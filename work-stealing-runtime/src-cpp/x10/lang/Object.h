#ifndef X10_LANG_OBJECT_H
#define X10_LANG_OBJECT_H

#include <x10aux/config.h>
#include <x10aux/ref.h>
#include <x10aux/RTT.h>

#include <x10aux/serialization.h>
#include <x10aux/deserialization_dispatcher.h>

namespace x10 {

    namespace lang {

        class String;
        class Ref;
        class Value;

        class Object {

            public:

#ifdef REF_COUNTING
            int __count; // Ref counting implementation
#endif

            class RTT : public x10aux::RuntimeType {
                public:
                static RTT* const it;
            
                virtual void init() { initParents(0); }

                virtual const char *name() const {
                    return "x10.lang.Object";
                }

            };

            virtual const x10aux::RuntimeType *_type() const {
                return x10aux::getRTT<Object>();
            }

            Object()
#ifdef REF_COUNTING
                : __count(0)
#endif      
            { }

            virtual ~Object() { }


            virtual void _serialize_id(x10aux::serialization_buffer &, x10aux::addr_map &) = 0;
            virtual void _serialize_body(x10aux::serialization_buffer &, x10aux::addr_map &) = 0;

            // This pair of functions should be overridden to not emit/extract the id in subclasses
            // that satisfy the following property:
            //
            //      "All instances of all my subclasses are de/serialised by the same code"
            //
            // Examples of classes that satisfy this property:  All final value classes, and Ref.
            //
            // These functions should not be overridden in subclasses of classes that override these
            // functions, i.e., any strict subclass of Ref.  If one is overridden, the other should
            // be too.
            //
            // Note these functions are static as we want to dispatch on the static type.

            static void _serialize(x10aux::ref<Object> this_,
                                   x10aux::serialization_buffer &buf,
                                   x10aux::addr_map &m);

            template<class T> static x10aux::ref<T> _deserialize(x10aux::serialization_buffer &buf){
                // extract the id and execute a callback to instantiate the right concrete class
                _S_("Deserializing an "ANSI_SER ANSI_BOLD"interface"ANSI_RESET
                    " (expecting id) from buf: "<<&buf);
                return x10aux::DeserializationDispatcher::create<T>(buf);
            }

            x10_boolean equals(x10aux::ref<Object> other);

            virtual x10_boolean equals(x10aux::ref<Ref> other) = 0;
            virtual x10_boolean equals(x10aux::ref<Value> other) = 0;
            virtual x10_int hashCode() = 0;
            virtual x10aux::ref<String> toString() = 0;

            // Needed for linking - non-values should not override
            virtual x10_boolean _struct_equals(x10aux::ref<Object> other) {
                if (other == x10aux::ref<Object>(this)) return true;
                return false;
            }

        };

    }

}

#endif
// vim:tabstop=4:shiftwidth=4:expandtab:textwidth=100