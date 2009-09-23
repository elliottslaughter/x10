import x10.io.Console;
import x10.io.File;
import x10.io.Marshal;
import x10.io.IOException;
import x10.util.Random;

public class KMeansSPMD {

    public static def printClusters (clusters:Rail[Float]!, dims:Int) {
        for (var d:Int=0 ; d<dims ; ++d) { 
            for (var k:Int=0 ; k<clusters.length/dims ; ++k) { 
                if (k>0)
                    Console.OUT.print(" ");
                Console.OUT.printf("%.2f",clusters(k*dims+d));
            }
            Console.OUT.println();
        }
    }

    public static def main (args : Rail[String]!) {

        var fname_:String = "points.dat";
        var DIM_:Int=3, CLUSTERS_:Int=8, POINTS_:Int=10000, ITERATIONS_:Int=500;
        switch (args.length) {
            case 5: ITERATIONS_ = Int.parseInt(args(4));
            case 4: DIM_        = Int.parseInt(args(3));
            case 3: CLUSTERS_   = Int.parseInt(args(2));
            case 2: POINTS_     = Int.parseInt(args(1));
            case 1: fname_      = args(0);
        }
        val fname = fname_, DIM = DIM_, CLUSTERS = CLUSTERS_,
            POINTS = POINTS_, ITERATIONS = ITERATIONS_;

        Console.OUT.println("points: "+POINTS+"  dim: "+DIM);
        val points_region = [0,0]..[POINTS-1,DIM-1];
        val clusters_region = [0,0]..[CLUSTERS-1,DIM-1];

        val points_dist = Dist.makeBlock(points_region, 0);

        try {

            val fr = (new File(fname)).openRead();
            val m = Marshal.INT;
            val init_points = (Int) => Float.fromIntBits(m.read(fr).reverseBytes());
            val points_cache = ValRail.make[Float](POINTS*DIM, init_points);
            val points = Array.make[Float](points_dist, (p:Point)=>points_cache(p(0)*DIM+p(1)));

            val central_clusters = Rail.makeVar[Float](CLUSTERS*DIM, (i:Int) => points_cache(i));
            // used to measure convergence at each iteration:
            val central_clusters_old =
                Rail.makeVar[Float](CLUSTERS*DIM, (i:Int) => central_clusters(i));
            val central_cluster_counts = Rail.makeVar[Int](CLUSTERS, (i:Int) => 0);

            class Cell[T] {
                private var value : T;
                def this(v:T) { this.value = v; }
                def get() = value;
                def set(v:T) { value = v; }
            }

            value ProxyCell[T] {
                private val cell : Cell[T]{self.at(this.loc)};
                def this(v:Cell[T]) { this.cell = v; }
                def get() = at (cell.location) cell.get();
                def set(v:T) { at (cell.location) cell.set(v); };
            }
                

            val finished = new Cell[Boolean](false);
            // SPMD style for algorithm
            val clk = Clock.make();

            val start_time = System.currentTimeMillis();

            finish {
                for (d in points_dist.places()) async (d) clocked(clk) {

                    val local_points = points.restriction(here) as Array[Float](2);

                    val clusters = Rail.makeVar[Float](CLUSTERS*DIM, (i:Int) => 0.0f);
                    val new_clusters = Rail.makeVar[Float](CLUSTERS*DIM, (i:Int) => 0.0f);
                    val cluster_counts = Rail.makeVar[Int](CLUSTERS, (i:Int) => 0);

                    val finished2 = new ProxyCell[Boolean](finished);

                    for (var iter:Int=0 ; iter<ITERATIONS && !finished2.get() ; iter++) {

                        Console.OUT.println("Iteration: "+iter);

                        // fetch the latest clusters
                        val home = here;
                        at (central_clusters.location) {
                            val central_clusters_copy = central_clusters as ValRail[Float];
                            at (home) {
                                for (var i:Int=0 ; i<CLUSTERS ; ++i) cluster_counts(i) = 0;
                                for (var j:Int=0 ; j<CLUSTERS*DIM ; ++j) {
                                    clusters(j) = central_clusters_copy(j);
                                    new_clusters(j) = 0;
                                }
                            }
                        }

                        // compute new clusters and counters
                        val min=local_points.region.min(0);
                        val max=local_points.region.max(0);
                        
                        for (var p:Int=min ; p<max ; ++p) {
                            var closest:Int = -1;
                            var closest_dist:Float = Float.MAX_VALUE;
                            for (var k:Int=0 ; k<CLUSTERS ; ++k) { 
                                var dist : Float = 0;
                                for (var d:Int=0 ; d<DIM ; ++d) { 
                                    val tmp = local_points(p,d) - clusters(k*DIM+d);
                                    dist += tmp * tmp;
                                }
                                if (dist < closest_dist) {
                                    closest_dist = dist;
                                    closest = k;
                                }
                            }
                            for (var d:Int=0 ; d<DIM ; ++d) { 
                                new_clusters(closest*DIM+d) += local_points(p,d);
                            }
                            cluster_counts(closest)++;
                        }


                        if (here == central_clusters.location) {
                            for (var j:Int=0 ; j<CLUSTERS ; ++j) central_cluster_counts(j) = 0;
                            for (var j:Int=0 ; j<DIM*CLUSTERS ; ++j) {
                                central_clusters_old(j) = central_clusters(j);
                                central_clusters(j) = 0;
                            }
                        }

                        next;


                        // have to create valrails for serialisation
                        val new_clusters_copy = new_clusters as ValRail[Float];
                        val cluster_counts_copy = cluster_counts as ValRail[Int];

                        at (central_clusters.location) atomic {
                            for (var j:Int=0 ; j<DIM*CLUSTERS ; ++j) {
                                central_clusters(j) += new_clusters_copy(j);
                            }
                            for (var j:Int=0 ; j<CLUSTERS ; ++j) {
                                central_cluster_counts(j) += cluster_counts_copy(j);
                            }
                        }


                        next;

                        if (here == central_clusters.location) {
                            for (var k:Int=0 ; k<CLUSTERS ; ++k) { 
                                for (var d:Int=0 ; d<DIM ; ++d) { 
                                    central_clusters(k*DIM+d) /= central_cluster_counts(k);
                                }
                            }

                            // TEST FOR CONVERGENCE
                            var b:Boolean = true;
                            finished.set(true);
                            for (var j:Int=0 ; j<CLUSTERS*DIM ; ++j) { 
                                if (Math.abs(central_clusters_old(j)-central_clusters(j))>0.0001) {
                                    finished.set(false);
                                    break;
                                }
                            }
                            
                        }



                    }


                }

                clk.drop();

            }

            printClusters(central_clusters,DIM);

            val stop_time = System.currentTimeMillis();
            Console.OUT.println("Time taken: "+(stop_time-start_time)/1E3);

        } catch (e : IOException) {
            Console.ERR.println("We had a little problem:");
            e.printStackTrace(Console.ERR);
            System.exit(1);
        }
    }
}

// vim: shiftwidth=4:tabstop=4:expandtab
