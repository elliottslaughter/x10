
//#line 18 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
//
// Licensed Material
// (C) Copyright IBM Corp, 2006
//

package x10.parser;

import lpg.runtime.*;

//#line 28 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

import polyglot.types.QName;
import polyglot.types.Name;
import polyglot.ast.AmbExpr;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.Call;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Case;
import polyglot.ast.Catch;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FloatLit;
import polyglot.ast.ForInit;
import polyglot.ast.ForUpdate;
import polyglot.ast.Formal;
import polyglot.ast.Id;
import polyglot.ast.Import;
import polyglot.ast.Initializer;
import polyglot.ast.IntLit;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.New;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Receiver;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.SwitchElement;
import polyglot.ast.TopLevelDecl;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.FlagsNode;
import polyglot.parse.ParsedName;
import polyglot.ext.x10.ast.AnnotationNode;
import polyglot.ext.x10.ast.Closure;
import polyglot.ext.x10.ast.ClosureCall;
import polyglot.ext.x10.ast.Here;
import polyglot.ext.x10.ast.DepParameterExpr;
import polyglot.ext.x10.ast.Tuple;
import polyglot.ext.x10.ast.When;
import polyglot.ext.x10.ast.X10Formal;
import polyglot.ext.x10.ast.X10Formal_c;
import polyglot.ext.x10.ast.X10Loop;
import polyglot.ext.x10.ast.X10Call;
import polyglot.ext.x10.ast.ConstantDistMaker;
import polyglot.ext.x10.ast.TypeDecl;
import polyglot.ext.x10.ast.TypeParamNode;
import polyglot.ext.x10.ast.TypePropertyNode;
import polyglot.ext.x10.ast.X10NodeFactory;
import polyglot.ext.x10.types.TypeProperty;
import polyglot.ext.x10.types.X10TypeSystem;
import polyglot.ext.x10.types.X10TypeSystem_c;
import polyglot.ext.x10.ast.PropertyDecl;
import polyglot.ext.x10.ast.RegionMaker;
import polyglot.ext.x10.extension.X10Ext;
import polyglot.frontend.FileSource;
import polyglot.frontend.Parser;
import polyglot.lex.BooleanLiteral;
import polyglot.lex.CharacterLiteral;
import polyglot.lex.DoubleLiteral;
import polyglot.lex.FloatLiteral;
import polyglot.lex.Identifier;
import polyglot.lex.IntegerLiteral;
import polyglot.lex.LongLiteral;
import polyglot.lex.NullLiteral;
import polyglot.lex.Operator;
import polyglot.lex.StringLiteral;
import polyglot.main.Report;
import polyglot.parse.VarDeclarator;
import polyglot.types.Flags;
import polyglot.ext.x10.types.X10Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;

import lpg.runtime.BacktrackingParser;
import lpg.runtime.BadParseException;
import lpg.runtime.BadParseSymFileException;
import lpg.runtime.DiagnoseParser;
import lpg.runtime.LexStream;
import lpg.runtime.NotBacktrackParseTableException;
import lpg.runtime.NullExportedSymbolsException;
import lpg.runtime.NullTerminalSymbolsException;
import lpg.runtime.ParseTable;
import lpg.runtime.PrsStream;
import lpg.runtime.RuleAction;
import lpg.runtime.UndefinedEofSymbolException;
import lpg.runtime.UnimplementedTerminalsException;

public class X10Parser extends PrsStream implements RuleAction, Parser
{
    private static ParseTable prs = new X10Parserprs();
    private BacktrackingParser btParser;

    public BacktrackingParser getParser() { return btParser; }
    private void setResult(Object object) { btParser.setSym1(object); }
    public Object getRhsSym(int i) { return btParser.getSym(i); }

    public int getRhsTokenIndex(int i) { return btParser.getToken(i); }
    public IToken getRhsIToken(int i) { return super.getIToken(getRhsTokenIndex(i)); }
    
    public int getRhsFirstTokenIndex(int i) { return btParser.getFirstToken(i); }
    public IToken getRhsFirstIToken(int i) { return super.getIToken(getRhsFirstTokenIndex(i)); }

    public int getRhsLastTokenIndex(int i) { return btParser.getLastToken(i); }
    public IToken getRhsLastIToken(int i) { return super.getIToken(getRhsLastTokenIndex(i)); }

    public int getLeftSpan() { return btParser.getFirstToken(); }
    public IToken getLeftIToken()  { return super.getIToken(getLeftSpan()); }

    public int getRightSpan() { return btParser.getLastToken(); }
    public IToken getRightIToken() { return super.getIToken(getRightSpan()); }

    public int getRhsErrorTokenIndex(int i)
    {
        int index = btParser.getToken(i);
        IToken err = super.getIToken(index);
        return (err instanceof ErrorToken ? index : 0);
    }
    public ErrorToken getRhsErrorIToken(int i)
    {
        int index = btParser.getToken(i);
        IToken err = super.getIToken(index);
        return (ErrorToken) (err instanceof ErrorToken ? err : null);
    }

    public X10Parser(ILexStream lexStream)
    {
        super(lexStream);

        try
        {
            super.remapTerminalSymbols(orderedTerminalSymbols(), X10Parserprs.EOFT_SYMBOL);
        }
        catch(NullExportedSymbolsException e) {
        }
        catch(NullTerminalSymbolsException e) {
        }
        catch(UnimplementedTerminalsException e)
        {
            java.util.ArrayList unimplemented_symbols = e.getSymbols();
            System.out.println("The Lexer will not scan the following token(s):");
            for (int i = 0; i < unimplemented_symbols.size(); i++)
            {
                Integer id = (Integer) unimplemented_symbols.get(i);
                System.out.println("    " + X10Parsersym.orderedTerminalSymbols[id.intValue()]);               
            }
            System.out.println();                        
        }
        catch(UndefinedEofSymbolException e)
        {
            throw new Error(new UndefinedEofSymbolException
                                ("The Lexer does not implement the Eof symbol " +
                                 X10Parsersym.orderedTerminalSymbols[X10Parserprs.EOFT_SYMBOL]));
        } 
    }

    public String[] orderedTerminalSymbols() { return X10Parsersym.orderedTerminalSymbols; }
    public String getTokenKindName(int kind) { return X10Parsersym.orderedTerminalSymbols[kind]; }
    public int getEOFTokenKind() { return X10Parserprs.EOFT_SYMBOL; }
    public PrsStream getParseStream() { return (PrsStream) this; }

    /**
     * When constructing a SAFARI parser, a handler for error messages
     * can be passed to the parser.
     */
    /*
    private IMessageHandler handler = null;
    public void setMessageHandler(IMessageHandler handler)
    {
        this.handler = handler;
    }
    
    //
    // Redirect syntax error message to proper recipient.
    //
    public void reportError(int error_code, String location_info, int left_token, int right_token, String token_text)
    {
        if (this.handler == null)
            super.reportError(error_code,
                              location_info,
                              left_token,
                              right_token,
                              token_text);
        else 
        {
            int start_offset = super.getStartOffset(left_token),
                end_offset = (right_token > left_token 
                                          ? super.getEndOffset(right_token)
                                          : super.getEndOffset(left_token));

            String msg = ((error_code == DELETION_CODE ||
                           error_code == MISPLACED_CODE ||
                           token_text.equals(""))
                                       ? ""
                                       : (token_text + " ")) +
                         errorMsgText[error_code];

            handler.handleMessage(start_offset,
                                  end_offset - start_offset + 1,
                                  msg);
        }
    }

    //
    // Report error message for given error_token.
    //
    public final void reportErrorTokenMessage(int error_token, String msg)
    {
        if (this.handler == null)
        {
            int firsttok = super.getFirstRealToken(error_token),
                lasttok = super.getLastRealToken(error_token);
            String location = super.getFileName() + ':' +
                              (firsttok > lasttok
                                        ? (super.getEndLine(lasttok) + ":" + super.getEndColumn(lasttok))
                                        : (super.getLine(error_token) + ":" +
                                           super.getColumn(error_token) + ":" +
                                           super.getEndLine(error_token) + ":" +
                                           super.getEndColumn(error_token)))
                              + ": ";
            super.reportError((firsttok > lasttok ? ParseErrorCodes.INSERTION_CODE : ParseErrorCodes.SUBSTITUTION_CODE), location, msg);
        }
        else 
        {
            handler.handleMessage(super.getStartOffset(error_token),
                                  super.getTokenLength(error_token),
                                  msg);
        }
    }
    */

    public polyglot.ast.Node parser()
    {
        return parser(null, Integer.MAX_VALUE);
    }
    
    public polyglot.ast.Node parser(Monitor monitor)
    {
        return parser(monitor, Integer.MAX_VALUE);
    }
    
    public polyglot.ast.Node parser(int error_repair_count)
    {
        return parser(null, error_repair_count);
    }

    public polyglot.ast.Node parser(Monitor monitor, int error_repair_count)
    {
        try
        {
            btParser = new BacktrackingParser(monitor, (TokenStream) this, prs, (RuleAction) this);
        }
        catch (NotBacktrackParseTableException e)
        {
            throw new Error(new NotBacktrackParseTableException
                                ("Regenerate X10Parserprs.java with -BACKTRACK option"));
        }
        catch (BadParseSymFileException e)
        {
            throw new Error(new BadParseSymFileException("Bad Parser Symbol File -- X10Parsersym.java"));
        }

        try
        {
            return (polyglot.ast.Node) btParser.fuzzyParse(error_repair_count);
        }
        catch (BadParseException e)
        {
            reset(e.error_token); // point to error token
            DiagnoseParser diagnoseParser = new DiagnoseParser(this, prs);
            diagnoseParser.diagnose(e.error_token);
        }

        return null;
    }


    //#line 310 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
    private ErrorQueue eq;
    private X10TypeSystem ts;
    private X10NodeFactory nf;
    private FileSource source;
    private boolean unrecoverableSyntaxError = false;

    public void initialize(TypeSystem t, NodeFactory n, FileSource source, ErrorQueue q)
    {
        this.ts = (X10TypeSystem) t;
        this.nf = (X10NodeFactory) n;
        this.source = source;
        this.eq = q;
    }

    public X10Parser(LexStream lexStream, TypeSystem t, NodeFactory n, FileSource source, ErrorQueue q)
    {
        this(lexStream);
        initialize((X10TypeSystem) t,
                   (X10NodeFactory) n,
                   source,
                   q);
        setMessageHandler(new MessageHandler(q));
    }

public static class MessageHandler implements IMessageHandler {
ErrorQueue eq;

public MessageHandler(ErrorQueue eq) {
    this.eq = eq;
}

public void handleMessage(int errorCode, int[] msgLocation,
                          int[] errorLocation, String filename,
                          String[] errorInfo) {

    File file = new File(filename);

    int l0 = msgLocation[2];
    int c0 = msgLocation[3];
    int l1 = msgLocation[4];
    int c1 = msgLocation[5];
    int o0 = msgLocation[0];
    int o1 = msgLocation[0] + msgLocation[1];

    Position pos = new Position(file.getPath(),
                file.getPath(), l0, c0, l1, c1+1, o0, o1);

    String msg = "";
    String info = "";

    for (String s : errorInfo) {
        info += s;
    }

    switch (errorCode) {
        case LEX_ERROR_CODE:
            msg = "Unexpected character ignored: " + info;
            break;
        case ERROR_CODE:
            msg = "Parse terminated at this token: " + info;
            break;
        case BEFORE_CODE:
            msg = "Token " + info + " expected before this input";
            break;
        case INSERTION_CODE:
            msg = "Token " + info + " expected after this input";
            break;
        case INVALID_CODE:
            msg = "Unexpected input discarded: " + info;
            break;
        case SUBSTITUTION_CODE:
            msg = "Token " + info + " expected instead of this input";
            break;
        case DELETION_CODE:
            msg = "Unexpected input ignored: " + info;
            break;
        case MERGE_CODE:
            msg = "Merging token(s) to recover: " + info;
            break;
        case MISPLACED_CODE:
            msg = "Misplaced constructs(s): " + info;
            break;
        case SCOPE_CODE:
            msg = "Token(s) inserted to complete scope: " + info;
            break;
        case EOF_CODE:
            msg = "Reached after this token: " + info;
            break;
        case INVALID_TOKEN_CODE:
            msg = "Invalid token: " + info;
            break;
        case ERROR_RULE_WARNING_CODE:
            msg = "Ignored token: " + info;
            break;
        case NO_MESSAGE_CODE:
            msg = "Syntax error";
            break;
    }

    eq.enqueue(ErrorInfo.SYNTAX_ERROR, msg, pos);
}
}

    public String getErrorLocation(int lefttok, int righttok)
    {
        return getFileName() + ':' +
               getLine(lefttok) + ":" + getColumn(lefttok) + ":" +
               getEndLine(righttok) + ":" + getEndColumn(righttok) + ": ";
    }

    public Position getErrorPosition(int lefttok, int righttok)
    {
        return new Position(null, getFileName(),
               getLine(lefttok), getColumn(lefttok),
               getEndLine(righttok), getEndColumn(righttok));
    }

    // RMF 11/7/2005 - N.B. This class has to be serializable, since it shows up inside Type objects,
    // which Polyglot serializes to save processing when loading class files generated from source
    // by Polyglot itself.
    public static class JPGPosition extends Position
    {
        private static final long serialVersionUID= -1593187800129872262L;
        private final transient IToken leftIToken,
                                       rightIToken;

        public JPGPosition(String path, String filename, IToken leftToken, IToken rightToken)
        {
            super(path, filename,
                  leftToken.getLine(), leftToken.getColumn(),
                  rightToken.getEndLine(), rightToken.getEndColumn(),
                  leftToken.getStartOffset(), rightToken.getEndOffset());
            this.leftIToken = null;
            this.rightIToken = null;
        }

        public IToken getLeftIToken() { return leftIToken; }
        public IToken getRightIToken() { return rightIToken; }

        public String toText()
        {
        if (true) return "...";
            IPrsStream prsStream = leftIToken.getPrsStream();
            return new String(prsStream.getInputChars(), offset(), endOffset() - offset() + 1);
        }
    }

    public polyglot.ast.Node parse() {
        try
        {
            SourceFile sf = (SourceFile) parser();

            if (sf != null)
            {
                if (! unrecoverableSyntaxError)
                    return sf.source(source);
                eq.enqueue(ErrorInfo.SYNTAX_ERROR, "Unable to parse " + source.name() + ".", new Position(null, file(), 1, 1, 1, 1));
            }   
        }
        catch (RuntimeException e) {
            // Let the Compiler catch and report it.
            throw e;
        }
        catch (Exception e) {
            // Used by cup to indicate a non-recoverable error.
            eq.enqueue(ErrorInfo.SYNTAX_ERROR, e.getMessage(), new Position(null, file(), 1, 1, 1, 1));
        }

        return null;
    }

    public String file()
    {
        return super.getFileName();
    }

    public JPGPosition pos()
    {
        return new JPGPosition("",
                               super.getFileName(),
                               super.getIToken(getLeftSpan()),
                               super.getIToken(getRightSpan()));
    }

    public JPGPosition pos(int i)
    {
        return new JPGPosition("",
                               super.getFileName(),
                               super.getIToken(i),
                               super.getIToken(i));
    }

    public JPGPosition pos(int i, int j)
    {
        return new JPGPosition("",
                               super.getFileName(),
                               super.getIToken(i),
                               super.getIToken(j));
    }

    /**
     * Return the source position of the declaration.
     */
    public JPGPosition pos (VarDeclarator n)
    {
      if (n == null) return null;
      return (JPGPosition) n.pos;
    }

    public JPGPosition pos(JPGPosition start, JPGPosition end) {
        return new JPGPosition(start.path(), start.file(), start.leftIToken, end.rightIToken);
    }

    private void checkTypeName(Id identifier) {
        String filename = file();
        String idname = identifier.id().toString();
        int dot = filename.lastIndexOf('.'),
            slash = filename.lastIndexOf('/', dot);
        if (slash == -1)
            slash = filename.lastIndexOf('\\', dot);
        String clean_filename = (slash >= 0 && dot >= 0 ? filename.substring(slash+1, dot) : "");
        if ((! clean_filename.equals(idname)) && clean_filename.equalsIgnoreCase(idname))
            eq.enqueue(ErrorInfo.SYNTAX_ERROR,
                       "This type name does not match the name of the containing file: " + filename.substring(slash+1),
                       identifier.position());
   }


    private polyglot.lex.Operator op(int i) {
        return new Operator(pos(i), super.getName(i), super.getKind(i));
    }

    private polyglot.lex.Identifier id(int i) {
        return new Identifier(pos(i), super.getName(i), X10Parsersym.TK_IDENTIFIER);
    }
    private String comment(int i) {
        String s = super.getName(i);
        if (s != null && s.startsWith("/**") && s.endsWith("*/")) {
            return s +"\n";
        }
        return null;
    }

    private List<Formal> toFormals(List<Formal> l) { return l; }

    private List<Expr> toActuals(List<Formal> l) {
        List<Expr> l2 = new ArrayList<Expr>();
        for (Formal f : l) {
            l2.add(nf.Local(f.position(), f.name()));
        }
        return l2;
    }

    private List<TypeParamNode> toTypeParams(List<TypeParamNode> l) { return l; }

    private List<TypeNode> toTypeArgs(List<TypeParamNode> l) {
        List<TypeNode> l2 = new ArrayList<TypeNode>();
        for (TypeParamNode f : l) {
            l2.add(nf.AmbTypeNode(f.position(), null, f.name()));
        }
        return l2;
    }

            
    private List extractAnnotations(List l) {
        List l2 = new LinkedList();
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof AnnotationNode) {
                l2.add((AnnotationNode) o);
            }
        }
        return l2;
    }

    private FlagsNode extractFlags(List l, Flags f) {
        FlagsNode fn = extractFlags(l);
        fn = fn.flags(fn.flags().set(f));
        return fn;
    }
    
    private FlagsNode extractFlags(List l1, List l2) {
        List l = new ArrayList();
        l.addAll(l1);
        l.addAll(l2);
        return extractFlags(l);
    }
    
    private FlagsNode extractFlags(List l) {
        Position pos = null;
        X10Flags xf = X10Flags.toX10Flags(Flags.NONE);
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof FlagsNode) {
                FlagsNode fn = (FlagsNode) o;
                pos = pos == null ? fn.position() : new Position(pos, fn.position());
                Flags f = fn.flags();
                if (f instanceof X10Flags) {
                    xf = xf.setX((X10Flags) f);
                }
                else {
                    xf = X10Flags.toX10Flags(xf.set(f));
                }
            }
        }
        return nf.FlagsNode(pos == null ? Position.COMPILER_GENERATED : pos, xf);
    }

    /* Roll our own integer parser.  We can't use Long.parseLong because
     * it doesn't handle numbers greater than 0x7fffffffffffffff correctly.
     */
    private long parseLong(String s, int radix)
    {
        long x = 0L;

        s = s.toLowerCase();

        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);

            if (c < '0' || c > '9') {
                c = c - 'a' + 10;
            }
            else {
                c = c - '0';
            }

            x *= radix;
            x += c;
        }

        return x;
    }

    private long parseLong(String s)
    {
        int radix,
            start_index,
            end_index = (s.charAt(s.length() - 1) == 'l' || s.charAt(s.length() - 1) == 'L'
                                                   ? s.length() - 1
                                                   : s.length());
        if (s.charAt(0) == '0')
        {
           if (s.length() > 1 && (s.charAt(1) == 'x' || s.charAt(1) == 'X'))
           {
               radix = 16;
               start_index = 2;
           }
           else
           {
               radix = 8;
               start_index = 0;
           }
        }
        else
        {
            radix = 10;
            start_index = 0;
        }

        return parseLong(s.substring(start_index, end_index), radix);
    }

    private polyglot.lex.LongLiteral int_lit(int i, int radix)
    {
        long x = parseLong(super.getName(i), radix);
        return new LongLiteral(pos(i),  x, X10Parsersym.TK_IntegerLiteral);
    }

    private polyglot.lex.LongLiteral int_lit(int i)
    {
        long x = parseLong(super.getName(i));
        return new LongLiteral(pos(i),  x, X10Parsersym.TK_IntegerLiteral);
    }

    private polyglot.lex.LongLiteral long_lit(int i, int radix)
    {
        long x = parseLong(super.getName(i), radix);
        return new LongLiteral(pos(i), x, X10Parsersym.TK_LongLiteral);
    }

    private polyglot.lex.LongLiteral long_lit(int i)
    {
        long x = parseLong(super.getName(i));
        return new LongLiteral(pos(i), x, X10Parsersym.TK_LongLiteral);
    }

    private polyglot.lex.FloatLiteral float_lit(int i)
    {
        try {
            String s = super.getName(i);
            int end_index = (s.charAt(s.length() - 1) == 'f' || s.charAt(s.length() - 1) == 'F'
                                                       ? s.length() - 1
                                                       : s.length());
            float x = Float.parseFloat(s.substring(0, end_index));
            return new FloatLiteral(pos(i), x, X10Parsersym.TK_FloatingPointLiteral);
        }
        catch (NumberFormatException e) {
            unrecoverableSyntaxError = true;
            eq.enqueue(ErrorInfo.LEXICAL_ERROR,
                       "Illegal float literal \"" + super.getName(i) + "\"", pos(i));
            return null;
        }
    }

    private polyglot.lex.DoubleLiteral double_lit(int i)
    {
        try {
            String s = super.getName(i);
            int end_index = (s.charAt(s.length() - 1) == 'd' || s.charAt(s.length() - 1) == 'D'
                                                       ? s.length() - 1
                                                       : s.length());
            double x = Double.parseDouble(s.substring(0, end_index));
            return new DoubleLiteral(pos(i), x, X10Parsersym.TK_DoubleLiteral);
        }
        catch (NumberFormatException e) {
            unrecoverableSyntaxError = true;
            eq.enqueue(ErrorInfo.LEXICAL_ERROR,
                       "Illegal float literal \"" + super.getName(i) + "\"", pos(i));
            return null;
        }
    }

    private polyglot.lex.CharacterLiteral char_lit(int i)
    {
        char x;
        String s = super.getName(i);
        if (s.charAt(1) == '\\') {
            switch(s.charAt(2)) {
                case 'u':
                    x = (char) parseLong(s.substring(3, s.length() - 1), 16);
                    break;
                case 'b':
                    x = '\b';
                    break;
                case 't':
                    x = '\t';
                    break;
                case 'n':
                    x = '\n';
                    break;
                case 'f':
                    x = '\f';
                    break;
                case 'r':
                    x = '\r';
                    break;
                case '\"':
                    x = '\"';
                    break;
                case '\'':
                    x = '\'';
                    break;
                case '\\':
                    x = '\\';
                    break;
                default:
                    x = (char) parseLong(s.substring(2, s.length() - 1), 8);
                    if (x > 255) {
                        unrecoverableSyntaxError = true;
                        eq.enqueue(ErrorInfo.LEXICAL_ERROR,
                                   "Illegal character literal " + s, pos(i));
                    }
            }
        }
        else {
            assert(s.length() == 3);
            x = s.charAt(1);
        }

        return new CharacterLiteral(pos(i), x, X10Parsersym.TK_CharacterLiteral);
    }

    private polyglot.lex.BooleanLiteral boolean_lit(int i)
    {
        return new BooleanLiteral(pos(i), super.getKind(i) == X10Parsersym.TK_true, super.getKind(i));
    }

    private polyglot.lex.StringLiteral string_lit(int i)
    {
        String s = super.getName(i);
        char x[] = new char[s.length()];
        int j = 1,
            k = 0;
        while(j < s.length() - 1) {
            if (s.charAt(j) != '\\')
                x[k++] = s.charAt(j++);
            else {
                switch(s.charAt(j + 1)) {
                    case 'u':
                        x[k++] = (char) parseLong(s.substring(j + 2, j + 6), 16);
                        j += 6;
                        break;
                    case 'b':
                        x[k++] = '\b';
                        j += 2;
                        break;
                    case 't':
                        x[k++] = '\t';
                        j += 2;
                        break;
                    case 'n':
                        x[k++] = '\n';
                        j += 2;
                        break;
                    case 'f':
                        x[k++] = '\f';
                        j += 2;
                        break;
                    case 'r':
                        x[k++] = '\r';
                        j += 2;
                        break;
                    case '\"':
                        x[k++] = '\"';
                        j += 2;
                        break;
                    case '\'':
                        x[k++] = '\'';
                        j += 2;
                        break;
                    case '\\':
                        x[k++] = '\\';
                        j += 2;
                        break;
                    default:
                    {
                        int n = j + 1;
                        for (int l = 0; l < 3 && Character.isDigit(s.charAt(n)); l++)
                            n++;
                        char c = (char) parseLong(s.substring(j + 1, n), 8);
                        if (c > 255) {
                            unrecoverableSyntaxError = true;
                            eq.enqueue(ErrorInfo.LEXICAL_ERROR,
                                       "Illegal character (" + s.substring(j, n) + ") in string literal " + s, pos(i));
                        }
                        x[k++] = c;
                        j = n;
                    }
                }
            }
        }

        return new StringLiteral(pos(i), new String(x, 0, k), X10Parsersym.TK_StringLiteral);
    }

    private polyglot.lex.NullLiteral null_lit(int i)
    {
        return new NullLiteral(pos(i), X10Parsersym.TK_null);
    }


    public void ruleAction(int ruleNumber)
    {
        switch (ruleNumber)
        {
 
            //
            // Rule 1:  TypeName ::= TypeName . ErrorId
            //
            case 1: {
                //#line 6 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 8 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      TypeName,
                                      nf.Id(pos(getRightSpan()), "*")));
                    break;
            }
         
            //
            // Rule 2:  PackageName ::= PackageName . ErrorId
            //
            case 2: {
                //#line 16 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName PackageName = (ParsedName) getRhsSym(1);
                //#line 18 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      PackageName,
                                      nf.Id(pos(getRightSpan()), "*")));
                    break;
            }
         
            //
            // Rule 3:  ExpressionName ::= AmbiguousName . ErrorId
            //
            case 3: {
                //#line 26 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 28 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      AmbiguousName,
                                      nf.Id(pos(getRightSpan()), "*")));
                    break;
            }
         
            //
            // Rule 4:  MethodName ::= AmbiguousName . ErrorId
            //
            case 4: {
                //#line 36 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 38 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      AmbiguousName,
                                      nf.Id(pos(getRightSpan()), "*")));
                    break;
            }
         
            //
            // Rule 5:  PackageOrTypeName ::= PackageOrTypeName . ErrorId
            //
            case 5: {
                //#line 46 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName PackageOrTypeName = (ParsedName) getRhsSym(1);
                //#line 48 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      PackageOrTypeName,
                                      nf.Id(pos(getRightSpan()), "*")));
                    break;
            }
         
            //
            // Rule 6:  AmbiguousName ::= AmbiguousName . ErrorId
            //
            case 6: {
                //#line 56 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 58 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    setResult(new ParsedName(nf,
                                      ts,
                                      pos(getLeftSpan(), getRightSpan()),
                                      AmbiguousName,
                                      nf.Id(pos(getRightSpan()), "*")));
                   break;
            }
         
            //
            // Rule 7:  FieldAccess ::= Primary . ErrorId
            //
            case 7: {
                //#line 66 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 68 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(), Primary,
                                      nf.Id(pos(getRightSpan()), "*")));
                break;
            }
     
            //
            // Rule 8:  FieldAccess ::= super . ErrorId
            //
            case 8: {
                
                //#line 74 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getLeftSpan())),
                                      nf.Id(pos(getRightSpan()), "*")));
                break;
            }
     
            //
            // Rule 9:  FieldAccess ::= ClassName . super$sup . ErrorId
            //
            case 9: {
                //#line 78 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 78 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 80 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getRhsFirstTokenIndex(3)), ClassName.toType()),
                                      nf.Id(pos(getRightSpan()), "*")));
                break;
            }
     
            //
            // Rule 10:  MethodInvocation ::= MethodPrimaryPrefix ( ArgumentListopt )
            //
            case 10: {
                //#line 85 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                Object MethodPrimaryPrefix = (Object) getRhsSym(1);
                //#line 85 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                List ArgumentListopt = (List) getRhsSym(3);
                //#line 87 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Expr Primary = (Expr) ((Object[]) MethodPrimaryPrefix)[0];
                polyglot.lex.Identifier identifier = (polyglot.lex.Identifier) ((Object[]) MethodPrimaryPrefix)[1];
                setResult(nf.Call(pos(), Primary, nf.Id(pos(), identifier.getIdentifier()), ArgumentListopt));
                break;
            }
     
            //
            // Rule 11:  MethodInvocation ::= MethodSuperPrefix ( ArgumentListopt )
            //
            case 11: {
                //#line 92 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                polyglot.lex.Identifier MethodSuperPrefix = (polyglot.lex.Identifier) getRhsSym(1);
                //#line 92 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                List ArgumentListopt = (List) getRhsSym(3);
                //#line 94 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.Identifier identifier = MethodSuperPrefix;
                setResult(nf.Call(pos(), nf.Super(pos(getLeftSpan())), nf.Id(pos(), identifier.getIdentifier()), ArgumentListopt));
                break;
            }
     
            //
            // Rule 12:  MethodInvocation ::= MethodClassNameSuperPrefix ( ArgumentListopt )
            //
            case 12: {
                //#line 98 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                Object MethodClassNameSuperPrefix = (Object) getRhsSym(1);
                //#line 98 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                List ArgumentListopt = (List) getRhsSym(3);
                //#line 100 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ParsedName ClassName = (ParsedName) ((Object[]) MethodClassNameSuperPrefix)[0];
                JPGPosition super_pos = (JPGPosition) ((Object[]) MethodClassNameSuperPrefix)[1];
                polyglot.lex.Identifier identifier = (polyglot.lex.Identifier) ((Object[]) MethodClassNameSuperPrefix)[2];
                setResult(nf.Call(pos(), nf.Super(super_pos, ClassName.toType()), nf.Id(pos(), identifier.getIdentifier()), ArgumentListopt));
                break;
            }
     
            //
            // Rule 13:  MethodPrimaryPrefix ::= Primary . ErrorId$ErrorId
            //
            case 13: {
                //#line 107 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 107 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                IToken ErrorId = (IToken) getRhsIToken(3);
                //#line 109 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Object[] a = new Object[2];
                a[0] = Primary;
                a[1] = id(getRhsFirstTokenIndex(3));
                setResult(a);
                break;
            }
     
            //
            // Rule 14:  MethodSuperPrefix ::= super . ErrorId$ErrorId
            //
            case 14: {
                //#line 115 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                IToken ErrorId = (IToken) getRhsIToken(3);
                //#line 117 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(id(getRhsFirstTokenIndex(3)));
                break;
            }
     
            //
            // Rule 15:  MethodClassNameSuperPrefix ::= ClassName . super$sup . ErrorId$ErrorId
            //
            case 15: {
                //#line 120 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 120 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 120 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/MissingId.gi"
                IToken ErrorId = (IToken) getRhsIToken(5);
                //#line 122 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Object[] a = new Object[3];
                a[0] = ClassName;
                a[1] = pos(getRhsFirstTokenIndex(3));
                a[2] = id(getRhsFirstTokenIndex(5));
                setResult(a);
                break;
            }
     
            //
            // Rule 16:  TypeDefDeclaration ::= TypeDefModifiersopt type Identifier TypeParametersopt FormalParametersopt WhereClauseopt = Type ;
            //
            case 16: {
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDefModifiersopt = (List) getRhsSym(1);
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(4);
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParametersopt = (List) getRhsSym(5);
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 865 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(8);
                //#line 867 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode f = extractFlags(TypeDefModifiersopt);
                List annotations = extractAnnotations(TypeDefModifiersopt);
                for (Formal v : (List<Formal>) FormalParametersopt) {
                    if (!v.flags().flags().isFinal()) throw new InternalCompilerError("Type definition parameters must be final.", v.position()); 
                }
                TypeDecl cd = nf.TypeDecl(pos(), f, Identifier, TypeParametersopt, FormalParametersopt, WhereClauseopt, Type);
                cd = (TypeDecl) ((X10Ext) cd.ext()).annotations(annotations);
                setResult(cd);
                break;
            }
     
            //
            // Rule 17:  TypeDefDeclaration ::= TypeDefModifiersopt type Identifier TypeParametersopt FormalParametersopt WhereClauseopt ;
            //
            case 17: {
                //#line 877 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDefModifiersopt = (List) getRhsSym(1);
                //#line 877 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 877 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(4);
                //#line 877 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParametersopt = (List) getRhsSym(5);
                //#line 877 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 879 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode f = extractFlags(TypeDefModifiersopt);
                List annotations = extractAnnotations(TypeDefModifiersopt);
                for (Formal v : (List<Formal>) FormalParametersopt) {
                    if (!v.flags().flags().isFinal()) throw new InternalCompilerError("Type definition parameters must be final.", v.position()); 
                }
                TypeDecl cd = nf.TypeDecl(pos(), f, Identifier, TypeParametersopt, FormalParametersopt, WhereClauseopt, null);
                cd = (TypeDecl) ((X10Ext) cd.ext()).annotations(annotations);
                setResult(cd);
                break;
            }
     
            //
            // Rule 18:  Properties ::= ( PropertyList )
            //
            case 18: {
                //#line 890 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List PropertyList = (List) getRhsSym(2);
                //#line 892 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
   setResult(PropertyList);
           break;
            }  
            //
            // Rule 19:  PropertyList ::= Property
            //
            case 19: {
                //#line 895 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                PropertyDecl Property = (PropertyDecl) getRhsSym(1);
                //#line 897 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), PropertyDecl.class, false);
                l.add(Property);
                setResult(l);
                break;
            }
     
            //
            // Rule 20:  PropertyList ::= PropertyList , Property
            //
            case 20: {
                //#line 902 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List PropertyList = (List) getRhsSym(1);
                //#line 902 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                PropertyDecl Property = (PropertyDecl) getRhsSym(3);
                //#line 904 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                PropertyList.add(Property);
                break;
            }
     
            //
            // Rule 21:  Property ::= Annotationsopt Identifier : Type
            //
            case 21: {
                //#line 909 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotationsopt = (List) getRhsSym(1);
                //#line 909 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(2);
                //#line 909 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(4);
                //#line 911 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List annotations = extractAnnotations(Annotationsopt);
                PropertyDecl cd = nf.PropertyDecl(pos(), nf.FlagsNode(pos(), Flags.PUBLIC.Final()), Type, Identifier);
                cd = (PropertyDecl) ((X10Ext) cd.ext()).annotations(annotations);
                setResult(cd);
                break;
            }
     
            //
            // Rule 22:  MethodDeclaration ::= MethodModifiersopt def Identifier TypeParametersopt FormalParameters WhereClauseopt ResultTypeopt Throwsopt MethodBody
            //
            case 22: {
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifiersopt = (List) getRhsSym(1);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(4);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameters = (List) getRhsSym(5);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(7);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Throwsopt = (List) getRhsSym(8);
                //#line 918 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block MethodBody = (Block) getRhsSym(9);
                //#line 920 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
       if (Identifier.id().toString().equals("this")) {
                   ConstructorDecl cd = nf.X10ConstructorDecl(pos(),
                                             extractFlags(MethodModifiersopt),
                                             nf.Id(pos(3), "this"),
                                             ResultTypeopt,
                                             TypeParametersopt,
                                             FormalParameters,
                                             WhereClauseopt,
                                             Throwsopt,
                                             MethodBody);
     cd = (ConstructorDecl) ((X10Ext) cd.ext()).annotations(extractAnnotations(MethodModifiersopt));
     setResult(cd);
          }
          else {
       MethodDecl md = nf.X10MethodDecl(pos(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(9)),
          extractFlags(MethodModifiersopt),
          
          ResultTypeopt == null ? nf.UnknownTypeNode(pos()) : ResultTypeopt,
          Identifier,
          TypeParametersopt,
          FormalParameters,
          WhereClauseopt,
          Throwsopt,
          MethodBody);
      md = (MethodDecl) ((X10Ext) md.ext()).annotations(extractAnnotations(MethodModifiersopt));
      setResult(md);
      }
                break;
            }
     
            //
            // Rule 23:  PropertyMethodDeclaration ::= MethodModifiersopt property Identifier TypeParametersopt FormalParameters WhereClauseopt ResultTypeopt Throwsopt MethodBody
            //
            case 23: {
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifiersopt = (List) getRhsSym(1);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(4);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameters = (List) getRhsSym(5);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(7);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Throwsopt = (List) getRhsSym(8);
                //#line 950 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block MethodBody = (Block) getRhsSym(9);
                //#line 952 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
       MethodDecl md = nf.X10MethodDecl(pos(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(9)),
          extractFlags(MethodModifiersopt, X10Flags.PROPERTY),
          ResultTypeopt == null ? nf.UnknownTypeNode(pos()) : ResultTypeopt,
          Identifier,
          TypeParametersopt,
          FormalParameters,
          WhereClauseopt,
          Throwsopt,
          MethodBody);
      md = (MethodDecl) ((X10Ext) md.ext()).annotations(extractAnnotations(MethodModifiersopt));
      setResult(md);
                break;
            }
     
            //
            // Rule 24:  PropertyMethodDeclaration ::= MethodModifiersopt property Identifier WhereClauseopt ResultTypeopt MethodBody
            //
            case 24: {
                //#line 965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifiersopt = (List) getRhsSym(1);
                //#line 965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(4);
                //#line 965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(5);
                //#line 965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block MethodBody = (Block) getRhsSym(6);
                //#line 967 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
       MethodDecl md = nf.X10MethodDecl(pos(getRhsFirstTokenIndex(1), getRhsLastTokenIndex(6)),
          extractFlags(MethodModifiersopt, X10Flags.PROPERTY),
          ResultTypeopt == null ? nf.UnknownTypeNode(pos()) : ResultTypeopt,
          Identifier,
          Collections.EMPTY_LIST,
          Collections.EMPTY_LIST,
          WhereClauseopt,
          Collections.EMPTY_LIST,
          MethodBody);
      md = (MethodDecl) ((X10Ext) md.ext()).annotations(extractAnnotations(MethodModifiersopt));
      setResult(md);
                break;
            }
     
            //
            // Rule 25:  ExplicitConstructorInvocation ::= this TypeArgumentsopt ( ArgumentListopt ) ;
            //
            case 25: {
                //#line 981 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 981 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(4);
                //#line 983 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10ThisCall(pos(), TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 26:  ExplicitConstructorInvocation ::= super TypeArgumentsopt ( ArgumentListopt ) ;
            //
            case 26: {
                //#line 986 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 986 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(4);
                //#line 988 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10SuperCall(pos(), TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 27:  ExplicitConstructorInvocation ::= Primary . this TypeArgumentsopt ( ArgumentListopt ) ;
            //
            case 27: {
                //#line 991 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 991 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(4);
                //#line 991 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(6);
                //#line 993 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10ThisCall(pos(), Primary, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 28:  ExplicitConstructorInvocation ::= Primary . super TypeArgumentsopt ( ArgumentListopt ) ;
            //
            case 28: {
                //#line 996 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 996 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(4);
                //#line 996 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(6);
                //#line 998 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10SuperCall(pos(), Primary, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 29:  NormalInterfaceDeclaration ::= InterfaceModifiersopt interface Identifier TypePropertiesopt Propertiesopt WhereClauseopt ExtendsInterfacesopt InterfaceBody
            //
            case 29: {
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceModifiersopt = (List) getRhsSym(1);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypePropertiesopt = (List) getRhsSym(4);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Propertiesopt = (List) getRhsSym(5);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExtendsInterfacesopt = (List) getRhsSym(7);
                //#line 1002 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody InterfaceBody = (ClassBody) getRhsSym(8);
                //#line 1004 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
      checkTypeName(Identifier);
      List TypeParametersopt = Collections.EMPTY_LIST;
      List/*<PropertyDecl>*/ props = Propertiesopt;
      DepParameterExpr ci = WhereClauseopt;
      FlagsNode fn = extractFlags(InterfaceModifiersopt, Flags.INTERFACE);
      ClassDecl cd = nf.X10ClassDecl(pos(),
                   fn,
                   Identifier,
                   TypeParametersopt,
                   TypePropertiesopt,
                   props,
                   ci,
                   null,
                   ExtendsInterfacesopt,
                   InterfaceBody);
      cd = (ClassDecl) ((X10Ext) cd.ext()).annotations(extractAnnotations(InterfaceModifiersopt));
      setResult(cd);
                break;
            }
     
            //
            // Rule 30:  ClassInstanceCreationExpression ::= new TypeName TypeArgumentsopt ( ArgumentListopt ) ClassBodyopt
            //
            case 30: {
                //#line 1024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(2);
                //#line 1024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(3);
                //#line 1024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(5);
                //#line 1024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody ClassBodyopt = (ClassBody) getRhsSym(7);
                //#line 1026 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (ClassBodyopt == null)
                     setResult(nf.X10New(pos(), TypeName.toType(), TypeArgumentsopt, ArgumentListopt));
                else setResult(nf.X10New(pos(), TypeName.toType(), TypeArgumentsopt, ArgumentListopt, ClassBodyopt));
                break;
            }
     
            //
            // Rule 31:  ClassInstanceCreationExpression ::= Primary . new Identifier TypeArgumentsopt ( ArgumentListopt ) ClassBodyopt
            //
            case 31: {
                //#line 1031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 1031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(4);
                //#line 1031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(5);
                //#line 1031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(7);
                //#line 1031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody ClassBodyopt = (ClassBody) getRhsSym(9);
                //#line 1033 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ParsedName b = new X10ParsedName(nf, ts, pos(), Identifier);
                if (ClassBodyopt == null)
                     setResult(nf.X10New(pos(), Primary, b.toType(), TypeArgumentsopt, ArgumentListopt));
                else setResult(nf.X10New(pos(), Primary, b.toType(), TypeArgumentsopt, ArgumentListopt, ClassBodyopt));
                break;
            }
     
            //
            // Rule 32:  ClassInstanceCreationExpression ::= AmbiguousName . new Identifier TypeArgumentsopt ( ArgumentListopt ) ClassBodyopt
            //
            case 32: {
                //#line 1039 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 1039 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(4);
                //#line 1039 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(5);
                //#line 1039 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(7);
                //#line 1039 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody ClassBodyopt = (ClassBody) getRhsSym(9);
                //#line 1041 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ParsedName b = new X10ParsedName(nf, ts, pos(), Identifier);
                if (ClassBodyopt == null)
                     setResult(nf.X10New(pos(), AmbiguousName.toExpr(), b.toType(), TypeArgumentsopt, ArgumentListopt));
                else setResult(nf.X10New(pos(), AmbiguousName.toExpr(), b.toType(), TypeArgumentsopt, ArgumentListopt, ClassBodyopt));
                break;
            }
     
            //
            // Rule 33:  AssignPropertyCall ::= property TypeArgumentsopt ( ArgumentListopt ) ;
            //
            case 33: {
                //#line 1048 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 1048 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(4);
                //#line 1050 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.AssignPropertyCall(pos(), TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 36:  FunctionType ::= TypeArgumentsopt ( FormalParameterListopt ) WhereClauseopt Throwsopt => Type
            //
            case 36: {
                //#line 1060 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(1);
                //#line 1060 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(3);
                //#line 1060 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(5);
                //#line 1060 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Throwsopt = (List) getRhsSym(6);
                //#line 1060 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(8);
                //#line 1062 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.FunctionTypeNode(pos(), TypeArgumentsopt, FormalParameterListopt, WhereClauseopt, Type, Throwsopt));
                break;
            }
     
            //
            // Rule 41:  AnnotatedType ::= Type Annotations
            //
            case 41: {
                //#line 1069 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(1);
                //#line 1069 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotations = (List) getRhsSym(2);
                //#line 1071 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                TypeNode tn = Type;
                tn = (TypeNode) ((X10Ext) tn.ext()).annotations((List<AnnotationNode>) Annotations);
                setResult(tn);
                break;
            }
     
            //
            // Rule 45:  ConstrainedType ::= ( Type )
            //
            case 45: {
                //#line 1080 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(2);
                //#line 1082 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Type);
                break;
            }
     
            //
            // Rule 46:  PlaceType ::= any
            //
            case 46: {
                
                //#line 1088 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(null);
                break;
            }
     
            //
            // Rule 47:  PlaceType ::= current
            //
            case 47: {
                
                //#line 1093 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(),
                                    nf.Field(pos(), nf.This(pos()), nf.Id(pos(), "loc")), Binary.EQ,
                                    nf.Field(pos(), nf.Self(pos()), nf.Id(pos(), "$current"))));
                break;
            }
     
            //
            // Rule 48:  PlaceType ::= PlaceExpression
            //
            case 48: {
                //#line 1098 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpression = (Expr) getRhsSym(1);
                //#line 1100 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(),
                                    nf.Field(pos(), nf.This(pos()), nf.Id(pos(), "loc")), Binary.EQ,
                                    PlaceExpression));
                break;
            }
     
            //
            // Rule 49:  NamedType ::= Primary . Identifier TypeArgumentsopt Argumentsopt DepParametersopt
            //
            case 49: {
                //#line 1106 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 1106 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 1106 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(4);
                //#line 1106 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Argumentsopt = (List) getRhsSym(5);
                //#line 1106 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr DepParametersopt = (DepParameterExpr) getRhsSym(6);
                //#line 1108 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            TypeNode type = nf.X10AmbTypeNode(pos(), Primary, Identifier);
            // TODO: place constraint
            if (DepParametersopt != null || (TypeArgumentsopt != null && ! TypeArgumentsopt.isEmpty()) || (Argumentsopt != null && ! Argumentsopt.isEmpty())) {
                type = nf.AmbDepTypeNode(pos(), Primary, Identifier, TypeArgumentsopt, Argumentsopt, DepParametersopt);
            }
            setResult(type);
                break;
            }
     
            //
            // Rule 50:  NamedType ::= TypeName TypeArgumentsopt Argumentsopt DepParametersopt
            //
            case 50: {
                //#line 1117 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 1117 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 1117 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Argumentsopt = (List) getRhsSym(3);
                //#line 1117 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr DepParametersopt = (DepParameterExpr) getRhsSym(4);
                //#line 1119 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            TypeNode type;
            
            if (TypeName.name.id().toString().equals("void")) {
                type = nf.CanonicalTypeNode(pos(), ts.Void());
            } else
            if (ts.isPrimitiveTypeName(TypeName.name.id())) {
                try {
                    type = nf.CanonicalTypeNode(pos(), ts.primitiveForName(TypeName.name.id()));
                }
                catch (SemanticException e) {
                    throw new InternalCompilerError("Unable to create primitive type for '" + TypeName.name.id() + "'!");
                }
            } else {
                type = TypeName.toType();
            }
            // TODO: place constraint
            if (DepParametersopt != null || (TypeArgumentsopt != null && ! TypeArgumentsopt.isEmpty()) || (Argumentsopt != null && ! Argumentsopt.isEmpty())) {
                type = nf.AmbDepTypeNode(pos(), TypeName.prefix != null ? TypeName.prefix.toPrefix() : null, TypeName.name, TypeArgumentsopt, Argumentsopt, DepParametersopt);
            }
            setResult(type);
                break;
            }
     
            //
            // Rule 51:  DepParameters ::= { ExistentialListopt Conjunction }
            //
            case 51: {
                //#line 1143 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExistentialListopt = (List) getRhsSym(2);
                //#line 1143 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Conjunction = (Expr) getRhsSym(3);
                //#line 1145 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.DepParameterExpr(pos(), ExistentialListopt, Conjunction));
                break;
            }
     
            //
            // Rule 52:  DepParameters ::= { ExistentialListopt Conjunction } !
            //
            case 52: {
                //#line 1148 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExistentialListopt = (List) getRhsSym(2);
                //#line 1148 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Conjunction = (Expr) getRhsSym(3);
                //#line 1150 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.DepParameterExpr(pos(), ExistentialListopt, Conjunction));
                break;
            }
     
            //
            // Rule 53:  DepParameters ::= { ExistentialListopt Conjunction } ! PlaceType
            //
            case 53: {
                //#line 1153 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExistentialListopt = (List) getRhsSym(2);
                //#line 1153 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Conjunction = (Expr) getRhsSym(3);
                //#line 1153 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceType = (Expr) getRhsSym(6);
                //#line 1155 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (PlaceType != null)
                    setResult(nf.DepParameterExpr(pos(), ExistentialListopt, nf.Binary(pos(), Conjunction, Binary.COND_AND, PlaceType)));
            else
		setResult(nf.DepParameterExpr(pos(), ExistentialListopt, Conjunction));
                break;
            }
     
            //
            // Rule 54:  TypeProperties ::= [ TypePropertyList ]
            //
            case 54: {
                //#line 1162 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypePropertyList = (List) getRhsSym(2);
                //#line 1164 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(TypePropertyList);
                break;
            }
     
            //
            // Rule 55:  TypeParameters ::= [ TypeParameterList ]
            //
            case 55: {
                //#line 1168 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParameterList = (List) getRhsSym(2);
                //#line 1170 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(TypeParameterList);
                break;
            }
     
            //
            // Rule 56:  FormalParameters ::= ( FormalParameterListopt )
            //
            case 56: {
                //#line 1173 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(2);
                //#line 1175 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(FormalParameterListopt);
                break;
            }
     
            //
            // Rule 57:  Conjunction ::= Expression
            //
            case 57: {
                //#line 1179 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(1);
                //#line 1181 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Expression);
                break;
            }
     
            //
            // Rule 58:  Conjunction ::= Conjunction , Expression
            //
            case 58: {
                //#line 1184 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Conjunction = (Expr) getRhsSym(1);
                //#line 1184 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1186 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), Conjunction, Binary.COND_AND, Expression));
                break;
            }
     
            //
            // Rule 59:  SubtypeConstraint ::= Type$t1 <: Type$t2
            //
            case 59: {
                //#line 1190 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t1 = (TypeNode) getRhsSym(1);
                //#line 1190 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t2 = (TypeNode) getRhsSym(3);
                //#line 1192 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.SubtypeTest(pos(), t1, t2, false));
                break;
            }
     
            //
            // Rule 60:  SubtypeConstraint ::= Type$t1 :> Type$t2
            //
            case 60: {
                //#line 1195 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t1 = (TypeNode) getRhsSym(1);
                //#line 1195 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t2 = (TypeNode) getRhsSym(3);
                //#line 1197 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.SubtypeTest(pos(), t2, t1, false));
                break;
            }
     
            //
            // Rule 61:  WhereClause ::= DepParameters
            //
            case 61: {
                //#line 1201 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr DepParameters = (DepParameterExpr) getRhsSym(1);
                //#line 1203 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            setResult(DepParameters);
                break;
            }
       
            //
            // Rule 62:  ExistentialListopt ::= $Empty
            //
            case 62: {
                
                //#line 1209 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            setResult(new ArrayList());
                break;
            }
       
            //
            // Rule 63:  ExistentialListopt ::= ExistentialList ;
            //
            case 63: {
                //#line 1212 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExistentialList = (List) getRhsSym(1);
                //#line 1214 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            setResult(ExistentialList);
                break;
            }
     
            //
            // Rule 64:  ExistentialList ::= FormalParameter
            //
            case 64: {
                //#line 1218 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal FormalParameter = (X10Formal) getRhsSym(1);
                //#line 1220 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Formal.class, false);
                l.add(FormalParameter.flags(nf.FlagsNode(pos(), Flags.FINAL)));
                setResult(l);
                break;
            }
     
            //
            // Rule 65:  ExistentialList ::= ExistentialList ; FormalParameter
            //
            case 65: {
                //#line 1225 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExistentialList = (List) getRhsSym(1);
                //#line 1225 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal FormalParameter = (X10Formal) getRhsSym(3);
                //#line 1227 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ExistentialList.add(FormalParameter.flags(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 68:  NormalClassDeclaration ::= ClassModifiersopt class Identifier TypePropertiesopt Propertiesopt WhereClauseopt Superopt Interfacesopt ClassBody
            //
            case 68: {
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassModifiersopt = (List) getRhsSym(1);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypePropertiesopt = (List) getRhsSym(4);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Propertiesopt = (List) getRhsSym(5);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Superopt = (TypeNode) getRhsSym(7);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Interfacesopt = (List) getRhsSym(8);
                //#line 1236 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody ClassBody = (ClassBody) getRhsSym(9);
                //#line 1238 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
      checkTypeName(Identifier);
                List TypeParametersopt = Collections.EMPTY_LIST;
      List/*<PropertyDecl>*/ props = Propertiesopt;
      DepParameterExpr ci = WhereClauseopt;
      FlagsNode f = extractFlags(ClassModifiersopt);
      List annotations = extractAnnotations(ClassModifiersopt);
      ClassDecl cd = nf.X10ClassDecl(pos(),
              f, Identifier, TypeParametersopt, TypePropertiesopt, props, ci, Superopt, Interfacesopt, ClassBody);
      cd = (ClassDecl) ((X10Ext) cd.ext()).annotations(annotations);
      setResult(cd);
                break;
            }
     
            //
            // Rule 69:  ValueClassDeclaration ::= ClassModifiersopt value Identifier TypePropertiesopt Propertiesopt WhereClauseopt Superopt Interfacesopt ClassBody
            //
            case 69: {
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassModifiersopt = (List) getRhsSym(1);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypePropertiesopt = (List) getRhsSym(4);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Propertiesopt = (List) getRhsSym(5);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Superopt = (TypeNode) getRhsSym(7);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Interfacesopt = (List) getRhsSym(8);
                //#line 1251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassBody ClassBody = (ClassBody) getRhsSym(9);
                //#line 1253 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
    checkTypeName(Identifier);
                List TypeParametersopt = Collections.EMPTY_LIST;
    List props = Propertiesopt;
    DepParameterExpr ci = WhereClauseopt;
    ClassDecl cd = (nf.X10ClassDecl(pos(getLeftSpan(), getRightSpan()),
    extractFlags(ClassModifiersopt, X10Flags.VALUE), Identifier,  TypeParametersopt,
    TypePropertiesopt, props, ci, Superopt, Interfacesopt, ClassBody));
    cd = (ClassDecl) ((X10Ext) cd.ext()).annotations(extractAnnotations(ClassModifiersopt));
    setResult(cd);
                break;
            }
     
            //
            // Rule 70:  ConstructorDeclaration ::= ConstructorModifiersopt def this TypeParametersopt FormalParameters WhereClauseopt ResultTypeopt Throwsopt ConstructorBody
            //
            case 70: {
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ConstructorModifiersopt = (List) getRhsSym(1);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(4);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameters = (List) getRhsSym(5);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(6);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(7);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Throwsopt = (List) getRhsSym(8);
                //#line 1265 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ConstructorBody = (Block) getRhsSym(9);
                //#line 1267 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
     ConstructorDecl cd = nf.X10ConstructorDecl(pos(),
                                             extractFlags(ConstructorModifiersopt),
                                             nf.Id(pos(3), "this"),
                                             ResultTypeopt,
                                             TypeParametersopt,
                                             FormalParameters,
                                             WhereClauseopt,
                                             Throwsopt,
                                             ConstructorBody);
     cd = (ConstructorDecl) ((X10Ext) cd.ext()).annotations(extractAnnotations(ConstructorModifiersopt));
     setResult(cd);
               break;
            }
    
            //
            // Rule 71:  Super ::= extends ClassType
            //
            case 71: {
                //#line 1281 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ClassType = (TypeNode) getRhsSym(2);
                //#line 1283 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ClassType);
                break;
            }
     
            //
            // Rule 72:  FieldKeyword ::= val
            //
            case 72: {
                
                //#line 1289 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 73:  FieldKeyword ::= var
            //
            case 73: {
                
                //#line 1294 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.NONE)));
                break;
            }
     
            //
            // Rule 74:  FieldKeyword ::= const
            //
            case 74: {
                
                //#line 1299 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL.Static())));
                break;
            }
     
            //
            // Rule 75:  VarKeyword ::= val
            //
            case 75: {
                
                //#line 1307 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 76:  VarKeyword ::= var
            //
            case 76: {
                
                //#line 1312 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.NONE)));
                break;
            }
     
            //
            // Rule 77:  FieldDeclaration ::= FieldModifiersopt FieldKeyword FieldDeclarators ;
            //
            case 77: {
                //#line 1317 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldModifiersopt = (List) getRhsSym(1);
                //#line 1317 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldKeyword = (List) getRhsSym(2);
                //#line 1317 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldDeclarators = (List) getRhsSym(3);
                //#line 1319 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                    FlagsNode fn = extractFlags(FieldModifiersopt, FieldKeyword);
    
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                    for (Iterator i = FieldDeclarators.iterator(); i.hasNext(); )
                    {
                        Object[] o = (Object[]) i.next();
                        Position pos = (Position) o[0];
                        Id name = (Id) o[1];
                        if (name == null) name = nf.Id(pos, Name.makeFresh());
                        List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                        if (type == null) type = nf.UnknownTypeNode(name.position());
                        Expr init = (Expr) o[5];
                        FieldDecl ld = nf.FieldDecl(pos, guard, fn,
                                           type, name, init);
                        ld = (FieldDecl) ((X10Ext) ld.ext()).annotations(extractAnnotations(FieldModifiersopt));
                        l.add(ld);
                    }
                setResult(l);
                break;
            }
     
            //
            // Rule 108:  IfThenStatement ::= if ( Expression ) Statement
            //
            case 108: {
                //#line 1376 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1376 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(5);
                //#line 1378 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.If(pos(), Expression, Statement));
                break;
            }
     
            //
            // Rule 109:  IfThenElseStatement ::= if ( Expression ) Statement$s1 else Statement$s2
            //
            case 109: {
                //#line 1382 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1382 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt s1 = (Stmt) getRhsSym(5);
                //#line 1382 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt s2 = (Stmt) getRhsSym(7);
                //#line 1384 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.If(pos(), Expression, s1, s2));
                break;
            }
     
            //
            // Rule 110:  EmptyStatement ::= ;
            //
            case 110: {
                
                //#line 1390 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Empty(pos()));
                break;
            }
     
            //
            // Rule 111:  LabeledStatement ::= Identifier : LoopStatement
            //
            case 111: {
                //#line 1394 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 1394 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt LoopStatement = (Stmt) getRhsSym(3);
                //#line 1396 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Labeled(pos(), Identifier, LoopStatement));
                break;
            }
     
            //
            // Rule 117:  ExpressionStatement ::= StatementExpression ;
            //
            case 117: {
                //#line 1406 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr StatementExpression = (Expr) getRhsSym(1);
                //#line 1408 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                boolean eval = true;
                if (StatementExpression instanceof X10Call) {
                    X10Call c = (X10Call) StatementExpression;
                    if (c.name().id().toString().equals("property") && c.target() == null) {
                        setResult(nf.AssignPropertyCall(c.position(),c.typeArguments(), c.arguments()));
                        eval = false;
                    }
                    if (c.name().id().toString().equals("super") && c.target() instanceof Expr) {
                        setResult(nf.X10SuperCall(c.position(), (Expr) c.target(), c.typeArguments(), c.arguments()));
                        eval = false;
                   }
                   if (c.name().id().toString().equals("this") && c.target() instanceof Expr) {
                        setResult(nf.X10ThisCall(c.position(), (Expr) c.target(), c.typeArguments(), c.arguments()));
                        eval = false;
                   }
                }
                    
                setResult(nf.Eval(pos(), StatementExpression));
                break;
            }
     
            //
            // Rule 125:  AssertStatement ::= assert Expression ;
            //
            case 125: {
                //#line 1437 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(2);
                //#line 1439 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Assert(pos(), Expression));
                break;
            }
     
            //
            // Rule 126:  AssertStatement ::= assert Expression$expr1 : Expression$expr2 ;
            //
            case 126: {
                //#line 1442 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr1 = (Expr) getRhsSym(2);
                //#line 1442 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr2 = (Expr) getRhsSym(4);
                //#line 1444 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Assert(pos(), expr1, expr2));
                break;
            }
     
            //
            // Rule 127:  SwitchStatement ::= switch ( Expression ) SwitchBlock
            //
            case 127: {
                //#line 1448 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1448 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchBlock = (List) getRhsSym(5);
                //#line 1450 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Switch(pos(), Expression, SwitchBlock));
                break;
            }
     
            //
            // Rule 128:  SwitchBlock ::= { SwitchBlockStatementGroupsopt SwitchLabelsopt }
            //
            case 128: {
                //#line 1454 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchBlockStatementGroupsopt = (List) getRhsSym(2);
                //#line 1454 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchLabelsopt = (List) getRhsSym(3);
                //#line 1456 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                SwitchBlockStatementGroupsopt.addAll(SwitchLabelsopt);
                setResult(SwitchBlockStatementGroupsopt);
                break;
            }
     
            //
            // Rule 130:  SwitchBlockStatementGroups ::= SwitchBlockStatementGroups SwitchBlockStatementGroup
            //
            case 130: {
                //#line 1462 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchBlockStatementGroups = (List) getRhsSym(1);
                //#line 1462 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchBlockStatementGroup = (List) getRhsSym(2);
                //#line 1464 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                SwitchBlockStatementGroups.addAll(SwitchBlockStatementGroup);
                // setResult(SwitchBlockStatementGroups);
                break;
            }
     
            //
            // Rule 131:  SwitchBlockStatementGroup ::= SwitchLabels BlockStatements
            //
            case 131: {
                //#line 1469 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchLabels = (List) getRhsSym(1);
                //#line 1469 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatements = (List) getRhsSym(2);
                //#line 1471 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), SwitchElement.class, false);
                l.addAll(SwitchLabels);
                l.add(nf.SwitchBlock(pos(), BlockStatements));
                setResult(l);
                break;
            }
     
            //
            // Rule 132:  SwitchLabels ::= SwitchLabel
            //
            case 132: {
                //#line 1478 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Case SwitchLabel = (Case) getRhsSym(1);
                //#line 1480 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Case.class, false);
                l.add(SwitchLabel);
                setResult(l);
                break;
            }
     
            //
            // Rule 133:  SwitchLabels ::= SwitchLabels SwitchLabel
            //
            case 133: {
                //#line 1485 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List SwitchLabels = (List) getRhsSym(1);
                //#line 1485 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Case SwitchLabel = (Case) getRhsSym(2);
                //#line 1487 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                SwitchLabels.add(SwitchLabel);
                //setResult(SwitchLabels);
                break;
            }
     
            //
            // Rule 134:  SwitchLabel ::= case ConstantExpression :
            //
            case 134: {
                //#line 1492 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConstantExpression = (Expr) getRhsSym(2);
                //#line 1494 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Case(pos(), ConstantExpression));
                break;
            }
     
            //
            // Rule 135:  SwitchLabel ::= default :
            //
            case 135: {
                
                //#line 1499 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Default(pos()));
                break;
            }
     
            //
            // Rule 136:  WhileStatement ::= while ( Expression ) Statement
            //
            case 136: {
                //#line 1503 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1503 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(5);
                //#line 1505 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.While(pos(), Expression, Statement));
                break;
            }
     
            //
            // Rule 137:  DoStatement ::= do Statement while ( Expression ) ;
            //
            case 137: {
                //#line 1509 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(2);
                //#line 1509 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(5);
                //#line 1511 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Do(pos(), Statement, Expression));
                break;
            }
     
            //
            // Rule 140:  BasicForStatement ::= for ( ForInitopt ; Expressionopt ; ForUpdateopt ) Statement
            //
            case 140: {
                //#line 1518 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ForInitopt = (List) getRhsSym(3);
                //#line 1518 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expressionopt = (Expr) getRhsSym(5);
                //#line 1518 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ForUpdateopt = (List) getRhsSym(7);
                //#line 1518 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(9);
                //#line 1520 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.For(pos(), ForInitopt, Expressionopt, ForUpdateopt, Statement));
                break;
            }
     
            //
            // Rule 142:  ForInit ::= LocalVariableDeclaration
            //
            case 142: {
                //#line 1525 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List LocalVariableDeclaration = (List) getRhsSym(1);
                //#line 1527 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ForInit.class, false);
                l.addAll(LocalVariableDeclaration);
                //setResult(l);
                break;
            }
     
            //
            // Rule 144:  StatementExpressionList ::= StatementExpression
            //
            case 144: {
                //#line 1535 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr StatementExpression = (Expr) getRhsSym(1);
                //#line 1537 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Eval.class, false);
                l.add(nf.Eval(pos(), StatementExpression));
                setResult(l);
                break;
            }
     
            //
            // Rule 145:  StatementExpressionList ::= StatementExpressionList , StatementExpression
            //
            case 145: {
                //#line 1542 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List StatementExpressionList = (List) getRhsSym(1);
                //#line 1542 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr StatementExpression = (Expr) getRhsSym(3);
                //#line 1544 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                StatementExpressionList.add(nf.Eval(pos(), StatementExpression));
                break;
            }
     
            //
            // Rule 146:  BreakStatement ::= break Identifieropt ;
            //
            case 146: {
                //#line 1548 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifieropt = (Id) getRhsSym(2);
                //#line 1550 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Break(pos(), Identifieropt));
                break;
            }
     
            //
            // Rule 147:  ContinueStatement ::= continue Identifieropt ;
            //
            case 147: {
                //#line 1554 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifieropt = (Id) getRhsSym(2);
                //#line 1556 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Continue(pos(), Identifieropt));
                break;
            }
     
            //
            // Rule 148:  ReturnStatement ::= return Expressionopt ;
            //
            case 148: {
                //#line 1560 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expressionopt = (Expr) getRhsSym(2);
                //#line 1562 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Return(pos(), Expressionopt));
                break;
            }
     
            //
            // Rule 149:  ThrowStatement ::= throw Expression ;
            //
            case 149: {
                //#line 1566 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(2);
                //#line 1568 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Throw(pos(), Expression));
                break;
            }
     
            //
            // Rule 150:  TryStatement ::= try Block Catches
            //
            case 150: {
                //#line 1572 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(2);
                //#line 1572 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Catches = (List) getRhsSym(3);
                //#line 1574 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Try(pos(), Block, Catches));
                break;
            }
     
            //
            // Rule 151:  TryStatement ::= try Block Catchesopt Finally
            //
            case 151: {
                //#line 1577 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(2);
                //#line 1577 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Catchesopt = (List) getRhsSym(3);
                //#line 1577 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Finally = (Block) getRhsSym(4);
                //#line 1579 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Try(pos(), Block, Catchesopt, Finally));
                break;
            }
     
            //
            // Rule 152:  Catches ::= CatchClause
            //
            case 152: {
                //#line 1583 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Catch CatchClause = (Catch) getRhsSym(1);
                //#line 1585 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Catch.class, false);
                l.add(CatchClause);
                setResult(l);
                break;
            }
     
            //
            // Rule 153:  Catches ::= Catches CatchClause
            //
            case 153: {
                //#line 1590 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Catches = (List) getRhsSym(1);
                //#line 1590 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Catch CatchClause = (Catch) getRhsSym(2);
                //#line 1592 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Catches.add(CatchClause);
                //setResult(Catches);
                break;
            }
     
            //
            // Rule 154:  CatchClause ::= catch ( FormalParameter ) Block
            //
            case 154: {
                //#line 1597 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal FormalParameter = (X10Formal) getRhsSym(3);
                //#line 1597 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(5);
                //#line 1599 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Catch(pos(), FormalParameter, Block));
                break;
            }
     
            //
            // Rule 155:  Finally ::= finally Block
            //
            case 155: {
                //#line 1603 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(2);
                //#line 1605 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Block);
                break;
            }
     
            //
            // Rule 156:  NowStatement ::= now ( Clock ) Statement
            //
            case 156: {
                //#line 1609 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Clock = (Expr) getRhsSym(3);
                //#line 1609 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(5);
                //#line 1611 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              setResult(nf.Now(pos(), Clock, Statement));
                break;
            }
     
            //
            // Rule 157:  ClockedClause ::= clocked ( ClockList )
            //
            case 157: {
                //#line 1615 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClockList = (List) getRhsSym(3);
                //#line 1617 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ClockList);
                break;
            }
     
            //
            // Rule 158:  AsyncStatement ::= async PlaceExpressionSingleListopt ClockedClauseopt Statement
            //
            case 158: {
                //#line 1621 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleListopt = (Expr) getRhsSym(2);
                //#line 1621 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClockedClauseopt = (List) getRhsSym(3);
                //#line 1621 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(4);
                //#line 1623 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              setResult(nf.Async(pos(), (PlaceExpressionSingleListopt == null
                                                                        ? nf.Here(pos(getLeftSpan()))
                                                                        : PlaceExpressionSingleListopt),
                                         ClockedClauseopt, Statement));
                break;
            }
     
            //
            // Rule 159:  AtStatement ::= at PlaceExpressionSingleList Statement
            //
            case 159: {
                //#line 1630 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(2);
                //#line 1630 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(3);
                //#line 1632 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              setResult(nf.AtStmt(pos(), PlaceExpressionSingleList, Statement));
                break;
            }
     
            //
            // Rule 160:  AtomicStatement ::= atomic Statement
            //
            case 160: {
                //#line 1636 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(2);
                //#line 1638 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              setResult(nf.Atomic(pos(), nf.Here(pos(getLeftSpan())), Statement));
                break;
            }
     
            //
            // Rule 161:  WhenStatement ::= when ( Expression ) Statement
            //
            case 161: {
                //#line 1643 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1643 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(5);
                //#line 1645 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.When(pos(), Expression, Statement));
                break;
            }
     
            //
            // Rule 162:  WhenStatement ::= WhenStatement or$or ( Expression ) Statement
            //
            case 162: {
                //#line 1648 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                When WhenStatement = (When) getRhsSym(1);
                //#line 1648 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken or = (IToken) getRhsIToken(2);
                //#line 1648 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(4);
                //#line 1648 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(6);
                //#line 1650 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              WhenStatement.addBranch(pos(getRhsFirstTokenIndex(2), getRightSpan()), Expression, Statement);
              setResult(WhenStatement);
                break;
            }
     
            //
            // Rule 163:  ForEachStatement ::= foreach ( LoopIndex in Expression ) ClockedClauseopt Statement
            //
            case 163: {
                //#line 1655 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal LoopIndex = (X10Formal) getRhsSym(3);
                //#line 1655 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(5);
                //#line 1655 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClockedClauseopt = (List) getRhsSym(7);
                //#line 1655 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(8);
                //#line 1657 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = LoopIndex.flags();
                Flags f = fn.flags();
                f = f.Final();
                fn = fn.flags(f);
                setResult(nf.ForEach(pos(),
                              LoopIndex.flags(fn),
                              Expression,
                              ClockedClauseopt,
                              Statement));
                break;
            }
     
            //
            // Rule 164:  AtEachStatement ::= ateach ( LoopIndex in Expression ) ClockedClauseopt Statement
            //
            case 164: {
                //#line 1669 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal LoopIndex = (X10Formal) getRhsSym(3);
                //#line 1669 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(5);
                //#line 1669 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClockedClauseopt = (List) getRhsSym(7);
                //#line 1669 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(8);
                //#line 1671 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = LoopIndex.flags();
                Flags f = fn.flags();
                f = f.Final();
                fn = fn.flags(f);
                setResult(nf.AtEach(pos(),
                             LoopIndex.flags(fn),
                             Expression,
                             ClockedClauseopt,
                             Statement));
                break;
            }
     
            //
            // Rule 165:  EnhancedForStatement ::= for ( LoopIndex in Expression ) Statement
            //
            case 165: {
                //#line 1683 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal LoopIndex = (X10Formal) getRhsSym(3);
                //#line 1683 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(5);
                //#line 1683 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(7);
                //#line 1685 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = LoopIndex.flags();
                Flags f = fn.flags();
                f = f.Final();
                fn = fn.flags(f);
                setResult(nf.ForLoop(pos(),
                        LoopIndex.flags(fn),
                        Expression,
                        Statement));
                break;
            }
     
            //
            // Rule 166:  FinishStatement ::= finish Statement
            //
            case 166: {
                //#line 1696 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(2);
                //#line 1698 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Finish(pos(),  Statement));
                break;
            }
     
            //
            // Rule 167:  AnnotationStatement ::= Annotations Statement
            //
            case 167: {
                //#line 1703 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotations = (List) getRhsSym(1);
                //#line 1703 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(2);
                //#line 1705 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (Statement.ext() instanceof X10Ext && Annotations instanceof List) {
                    Statement = (Stmt) ((X10Ext) Statement.ext()).annotations((List) Annotations);
                }
                setResult(Statement);
                break;
            }
     
            //
            // Rule 168:  PlaceExpressionSingleList ::= ( PlaceExpression )
            //
            case 168: {
                //#line 1712 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpression = (Expr) getRhsSym(2);
                //#line 1714 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              setResult(PlaceExpression);
                break;
            }
     
            //
            // Rule 170:  NextStatement ::= next ;
            //
            case 170: {
                
                //#line 1722 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Next(pos()));
                break;
            }
     
            //
            // Rule 171:  AwaitStatement ::= await Expression ;
            //
            case 171: {
                //#line 1726 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(2);
                //#line 1728 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Await(pos(), Expression));
                break;
            }
     
            //
            // Rule 172:  ClockList ::= Clock
            //
            case 172: {
                //#line 1732 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Clock = (Expr) getRhsSym(1);
                //#line 1734 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Expr.class, false);
                l.add(Clock);
                setResult(l);
                break;
            }
     
            //
            // Rule 173:  ClockList ::= ClockList , Clock
            //
            case 173: {
                //#line 1739 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClockList = (List) getRhsSym(1);
                //#line 1739 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Clock = (Expr) getRhsSym(3);
                //#line 1741 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ClockList.add(Clock);
                setResult(ClockList);
                break;
            }
     
            //
            // Rule 174:  Clock ::= Expression
            //
            case 174: {
                //#line 1747 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(1);
                //#line 1749 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
    setResult(Expression);
                break;
            }
     
            //
            // Rule 175:  CastExpression ::= ConditionalExpression as Type
            //
            case 175: {
                //#line 1761 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalExpression = (Expr) getRhsSym(1);
                //#line 1761 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1763 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Cast(pos(), Type, ConditionalExpression, false));
                break;
            }
     
            //
            // Rule 176:  CastExpression ::= ConditionalExpression to Type
            //
            case 176: {
                //#line 1766 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalExpression = (Expr) getRhsSym(1);
                //#line 1766 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1768 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Cast(pos(), Type, ConditionalExpression, true));
                break;
            }
     
            //
            // Rule 177:  CastExpression ::= ConditionalExpression ! Expression
            //
            case 177: {
                //#line 1771 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalExpression = (Expr) getRhsSym(1);
                //#line 1771 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 1773 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.PlaceCast(pos(), Expression, ConditionalExpression));
                break;
            }
     
            //
            // Rule 179:  TypePropertyList ::= TypeProperty
            //
            case 179: {
                //#line 1780 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypePropertyNode TypeProperty = (TypePropertyNode) getRhsSym(1);
                //#line 1782 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TypePropertyNode.class, false);
                l.add(TypeProperty);
                setResult(l);
                break;
            }
     
            //
            // Rule 180:  TypePropertyList ::= TypePropertyList , TypeProperty
            //
            case 180: {
                //#line 1787 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypePropertyList = (List) getRhsSym(1);
                //#line 1787 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypePropertyNode TypeProperty = (TypePropertyNode) getRhsSym(3);
                //#line 1789 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                TypePropertyList.add(TypeProperty);
                setResult(TypePropertyList);
                break;
            }
     
            //
            // Rule 181:  TypeParameterList ::= TypeParameter
            //
            case 181: {
                //#line 1794 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeParamNode TypeParameter = (TypeParamNode) getRhsSym(1);
                //#line 1796 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TypeParamNode.class, false);
                l.add(TypeParameter);
                setResult(l);
                break;
            }
     
            //
            // Rule 182:  TypeParameterList ::= TypeParameterList , TypeParameter
            //
            case 182: {
                //#line 1801 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParameterList = (List) getRhsSym(1);
                //#line 1801 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeParamNode TypeParameter = (TypeParamNode) getRhsSym(3);
                //#line 1803 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                TypeParameterList.add(TypeParameter);
                setResult(TypeParameterList);
                break;
            }
     
            //
            // Rule 183:  TypeProperty ::= Identifier
            //
            case 183: {
                //#line 1808 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 1810 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.TypePropertyNode(pos(), Identifier, TypeProperty.Variance.INVARIANT));
                break;
            }
     
            //
            // Rule 184:  TypeProperty ::= + Identifier
            //
            case 184: {
                //#line 1813 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(2);
                //#line 1815 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.TypePropertyNode(pos(), Identifier, TypeProperty.Variance.COVARIANT));
                break;
            }
     
            //
            // Rule 185:  TypeProperty ::= - Identifier
            //
            case 185: {
                //#line 1818 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(2);
                //#line 1820 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.TypePropertyNode(pos(), Identifier, TypeProperty.Variance.CONTRAVARIANT));
                break;
            }
     
            //
            // Rule 186:  TypeParameter ::= Identifier
            //
            case 186: {
                //#line 1824 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 1826 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.TypeParamNode(pos(), Identifier));
                break;
            }
     
            //
            // Rule 187:  Primary ::= here
            //
            case 187: {
                
                //#line 1832 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(((X10NodeFactory) nf).Here(pos()));
                break;
            }
     
            //
            // Rule 189:  RegionExpressionList ::= RegionExpression
            //
            case 189: {
                //#line 1838 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RegionExpression = (Expr) getRhsSym(1);
                //#line 1840 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Expr.class, false);
                l.add(RegionExpression);
                setResult(l);
                break;
            }
     
            //
            // Rule 190:  RegionExpressionList ::= RegionExpressionList , RegionExpression
            //
            case 190: {
                //#line 1845 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List RegionExpressionList = (List) getRhsSym(1);
                //#line 1845 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RegionExpression = (Expr) getRhsSym(3);
                //#line 1847 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                RegionExpressionList.add(RegionExpression);
                //setResult(RegionExpressionList);
                break;
            }
     
            //
            // Rule 191:  Primary ::= [ ArgumentList ]
            //
            case 191: {
                //#line 1852 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentList = (List) getRhsSym(2);
                //#line 1854 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Tuple tuple = nf.Tuple(pos(), ArgumentList);
                setResult(tuple);
                break;
            }
     
            //
            // Rule 192:  AssignmentExpression ::= Expression$expr1 -> Expression$expr2
            //
            case 192: {
                //#line 1859 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr1 = (Expr) getRhsSym(1);
                //#line 1859 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr2 = (Expr) getRhsSym(3);
                //#line 1861 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Expr call = nf.ConstantDistMaker(pos(), expr1, expr2);
                setResult(call);
                break;
            }
     
            //
            // Rule 193:  ClosureExpression ::= TypeParametersopt FormalParameters WhereClauseopt ResultTypeopt Throwsopt => ClosureBody
            //
            case 193: {
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(1);
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameters = (List) getRhsSym(2);
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(3);
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(4);
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Throwsopt = (List) getRhsSym(5);
                //#line 1866 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(7);
                //#line 1868 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Closure(pos(), TypeParametersopt, FormalParameters, WhereClauseopt, 
          ResultTypeopt == null ? nf.UnknownTypeNode(pos()) : ResultTypeopt, Throwsopt, ClosureBody));
                break;
            }
     
            //
            // Rule 194:  LastExpression ::= Expression
            //
            case 194: {
                //#line 1873 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(1);
                //#line 1875 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Return(pos(), Expression, true));
                break;
            }
     
            //
            // Rule 195:  ClosureBody ::= CastExpression
            //
            case 195: {
                //#line 1879 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr CastExpression = (Expr) getRhsSym(1);
                //#line 1881 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Block(pos(), nf.X10Return(pos(), CastExpression, true)));
                break;
            }
     
            //
            // Rule 196:  ClosureBody ::= { BlockStatementsopt LastExpression }
            //
            case 196: {
                //#line 1884 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatementsopt = (List) getRhsSym(2);
                //#line 1884 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt LastExpression = (Stmt) getRhsSym(3);
                //#line 1886 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Stmt> l = new ArrayList<Stmt>();
                l.addAll(BlockStatementsopt);
                l.add(LastExpression);
                setResult(nf.Block(pos(), l));
                break;
            }
     
            //
            // Rule 198:  AtExpression ::= at PlaceExpressionSingleList ClosureBody
            //
            case 198: {
                //#line 1894 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(2);
                //#line 1894 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(3);
                //#line 1896 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
              Block body = nf.Block(pos(), nf.AtStmt(pos(), PlaceExpressionSingleList, ClosureBody));
              Closure c = nf.Closure(pos(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, (DepParameterExpr) null, nf.UnknownTypeNode(pos()), Collections.EMPTY_LIST, body);
              setResult(nf.ClosureCall(pos(), c, Collections.EMPTY_LIST, Collections.EMPTY_LIST));
                break;
            }
     
            //
            // Rule 199:  AsyncExpression ::= async ClosureBody
            //
            case 199: {
                //#line 1902 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(2);
                //#line 1904 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Call(pos(), nf.Future(pos(), nf.Here(pos(getLeftSpan())), nf.UnknownTypeNode(pos()), ClosureBody), nf.Id(pos(), "force")));
                break;
            }
     
            //
            // Rule 200:  AsyncExpression ::= async PlaceExpressionSingleList ClosureBody
            //
            case 200: {
                //#line 1907 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(2);
                //#line 1907 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(3);
                //#line 1909 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Call(pos(), nf.Future(pos(), PlaceExpressionSingleList, nf.UnknownTypeNode(pos()), ClosureBody), nf.Id(pos(), "force")));
                break;
            }
     
            //
            // Rule 201:  AsyncExpression ::= async [ Type ] ClosureBody
            //
            case 201: {
                //#line 1912 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1912 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(5);
                //#line 1914 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Call(pos(), nf.Future(pos(), nf.Here(pos(getLeftSpan())), Type, ClosureBody), nf.Id(pos(), "force")));
                break;
            }
     
            //
            // Rule 202:  AsyncExpression ::= async [ Type ] PlaceExpressionSingleList ClosureBody
            //
            case 202: {
                //#line 1917 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1917 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(5);
                //#line 1917 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(6);
                //#line 1919 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Call(pos(), nf.Future(pos(), PlaceExpressionSingleList, Type, ClosureBody), nf.Id(pos(), "force")));
                break;
            }
     
            //
            // Rule 203:  FutureExpression ::= future ClosureBody
            //
            case 203: {
                //#line 1923 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(2);
                //#line 1925 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Future(pos(), nf.Here(pos(getLeftSpan())), nf.UnknownTypeNode(pos()), ClosureBody));
                break;
            }
     
            //
            // Rule 204:  FutureExpression ::= future PlaceExpressionSingleList ClosureBody
            //
            case 204: {
                //#line 1928 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(2);
                //#line 1928 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(3);
                //#line 1930 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Future(pos(), PlaceExpressionSingleList, nf.UnknownTypeNode(pos()), ClosureBody));
                break;
            }
     
            //
            // Rule 205:  FutureExpression ::= future [ Type ] ClosureBody
            //
            case 205: {
                //#line 1933 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1933 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(5);
                //#line 1935 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Future(pos(), nf.Here(pos(getLeftSpan())), Type, ClosureBody));
                break;
            }
     
            //
            // Rule 206:  FutureExpression ::= future [ Type ] PlaceExpressionSingleList ClosureBody
            //
            case 206: {
                //#line 1938 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 1938 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PlaceExpressionSingleList = (Expr) getRhsSym(5);
                //#line 1938 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ClosureBody = (Block) getRhsSym(6);
                //#line 1940 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Future(pos(), PlaceExpressionSingleList, Type, ClosureBody));
                break;
            }
     
            //
            // Rule 207:  DepParametersopt ::= $Empty
            //
            case 207:
                setResult(null);
                break;
 
            //
            // Rule 209:  PropertyListopt ::= $Empty
            //
            case 209: {
                
                //#line 1951 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), PropertyDecl.class, false));
                break;
            }
     
            //
            // Rule 211:  WhereClauseopt ::= $Empty
            //
            case 211:
                setResult(null);
                break;
 
            //
            // Rule 213:  ObjectKindopt ::= $Empty
            //
            case 213:
                setResult(null);
                break;
 
            //
            // Rule 215:  PlaceExpressionSingleListopt ::= $Empty
            //
            case 215:
                setResult(null);
                break;
 
            //
            // Rule 217:  ClassModifiersopt ::= $Empty
            //
            case 217: {
                
                //#line 1970 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
         setResult(Collections.singletonList(nf.FlagsNode(Position.COMPILER_GENERATED, X10Flags.toX10Flags(Flags.NONE))));
                break;
            }  
            //
            // Rule 219:  TypeDefModifiersopt ::= $Empty
            //
            case 219: {
                
                //#line 1976 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
         setResult(Collections.singletonList(nf.FlagsNode(Position.COMPILER_GENERATED, X10Flags.toX10Flags(Flags.NONE))));
                break;
            }  
            //
            // Rule 221:  Unsafeopt ::= $Empty
            //
            case 221:
                setResult(null);
                break;
 
            //
            // Rule 222:  Unsafeopt ::= unsafe
            //
            case 222: {
                
                //#line 1984 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                // any value distinct from null
                setResult(this);
                break;
            }
     
            //
            // Rule 223:  ClockedClauseopt ::= $Empty
            //
            case 223: {
                
                //#line 1991 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Expr.class, false));
                break;
            }
     
            //
            // Rule 225:  identifier ::= IDENTIFIER$ident
            //
            case 225: {
                //#line 2000 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken ident = (IToken) getRhsIToken(1);
                //#line 2002 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ident.setKind(X10Parsersym.TK_IDENTIFIER);
                setResult(id(getRhsFirstTokenIndex(1)));
                break;
            }
     
            //
            // Rule 226:  TypeName ::= Identifier
            //
            case 226: {
                //#line 2007 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2009 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 227:  TypeName ::= TypeName . Identifier
            //
            case 227: {
                //#line 2012 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 2012 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2014 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  TypeName,
                                  Identifier));
                break;
            }
     
            //
            // Rule 229:  TypeArguments ::= [ TypeArgumentList ]
            //
            case 229: {
                //#line 2024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentList = (List) getRhsSym(2);
                //#line 2026 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(TypeArgumentList);
                break;
            }
     
            //
            // Rule 230:  TypeArgumentList ::= Type
            //
            case 230: {
                //#line 2031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(1);
                //#line 2033 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new ArrayList();
                l.add(Type);
                setResult(l);
                break;
            }
     
            //
            // Rule 231:  TypeArgumentList ::= TypeArgumentList , Type
            //
            case 231: {
                //#line 2038 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentList = (List) getRhsSym(1);
                //#line 2038 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 2040 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                TypeArgumentList.add(Type);
                break;
            }
     
            //
            // Rule 232:  PackageName ::= Identifier
            //
            case 232: {
                //#line 2048 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2050 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 233:  PackageName ::= PackageName . Identifier
            //
            case 233: {
                //#line 2053 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName PackageName = (ParsedName) getRhsSym(1);
                //#line 2053 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2055 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  PackageName,
                                  Identifier));
                break;
            }
     
            //
            // Rule 234:  ExpressionName ::= Identifier
            //
            case 234: {
                //#line 2069 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2071 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 235:  ExpressionName ::= AmbiguousName . Identifier
            //
            case 235: {
                //#line 2074 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 2074 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2076 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  AmbiguousName,
                                  Identifier));
                break;
            }
     
            //
            // Rule 236:  MethodName ::= Identifier
            //
            case 236: {
                //#line 2084 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2086 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 237:  MethodName ::= AmbiguousName . Identifier
            //
            case 237: {
                //#line 2089 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 2089 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2091 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  AmbiguousName,
                                  Identifier));
                break;
            }
     
            //
            // Rule 238:  PackageOrTypeName ::= Identifier
            //
            case 238: {
                //#line 2099 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2101 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 239:  PackageOrTypeName ::= PackageOrTypeName . Identifier
            //
            case 239: {
                //#line 2104 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName PackageOrTypeName = (ParsedName) getRhsSym(1);
                //#line 2104 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2106 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  PackageOrTypeName,
                                  Identifier));
                break;
            }
     
            //
            // Rule 240:  AmbiguousName ::= Identifier
            //
            case 240: {
                //#line 2114 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2116 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 241:  AmbiguousName ::= AmbiguousName . Identifier
            //
            case 241: {
                //#line 2119 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName AmbiguousName = (ParsedName) getRhsSym(1);
                //#line 2119 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 2121 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf,
                                  ts,
                                  pos(getLeftSpan(), getRightSpan()),
                                  AmbiguousName,
                                  Identifier));
               break;
            }
     
            //
            // Rule 242:  CompilationUnit ::= PackageDeclarationopt ImportDeclarationsopt TypeDeclarationsopt
            //
            case 242: {
                //#line 2131 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                PackageNode PackageDeclarationopt = (PackageNode) getRhsSym(1);
                //#line 2131 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ImportDeclarationsopt = (List) getRhsSym(2);
                //#line 2131 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDeclarationsopt = (List) getRhsSym(3);
                //#line 2133 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                // Add import x10.lang.* by default.
                int token_pos = (ImportDeclarationsopt.size() == 0
                                     ? TypeDeclarationsopt.size() == 0
                                           ? super.getSize() - 1
                                           : getPrevious(getRhsFirstTokenIndex(3))
                                 : getRhsLastTokenIndex(2)
                            );
                Import x10LangImport = 
                nf.Import(pos(token_pos), Import.PACKAGE, QName.make("x10.lang"));
                ImportDeclarationsopt.add(x10LangImport);
                setResult(nf.SourceFile(pos(getLeftSpan(), getRightSpan()), PackageDeclarationopt, ImportDeclarationsopt, TypeDeclarationsopt));
                break;
            }
     
            //
            // Rule 243:  ImportDeclarations ::= ImportDeclaration
            //
            case 243: {
                //#line 2147 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Import ImportDeclaration = (Import) getRhsSym(1);
                //#line 2149 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Import.class, false);
                l.add(ImportDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 244:  ImportDeclarations ::= ImportDeclarations ImportDeclaration
            //
            case 244: {
                //#line 2154 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ImportDeclarations = (List) getRhsSym(1);
                //#line 2154 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Import ImportDeclaration = (Import) getRhsSym(2);
                //#line 2156 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (ImportDeclaration != null)
                    ImportDeclarations.add(ImportDeclaration);
                //setResult(l);
                break;
            }
     
            //
            // Rule 245:  TypeDeclarations ::= TypeDeclaration
            //
            case 245: {
                //#line 2162 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TopLevelDecl TypeDeclaration = (TopLevelDecl) getRhsSym(1);
                //#line 2164 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TopLevelDecl.class, false);
                if (TypeDeclaration != null)
                    l.add(TypeDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 246:  TypeDeclarations ::= TypeDeclarations TypeDeclaration
            //
            case 246: {
                //#line 2170 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDeclarations = (List) getRhsSym(1);
                //#line 2170 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TopLevelDecl TypeDeclaration = (TopLevelDecl) getRhsSym(2);
                //#line 2172 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (TypeDeclaration != null)
                    TypeDeclarations.add(TypeDeclaration);
                //setResult(l);
                break;
            }
     
            //
            // Rule 247:  PackageDeclaration ::= Annotationsopt package PackageName ;
            //
            case 247: {
                //#line 2178 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotationsopt = (List) getRhsSym(1);
                //#line 2178 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName PackageName = (ParsedName) getRhsSym(3);
                //#line 2180 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                PackageNode pn = PackageName.toPackage();
                pn = (PackageNode) ((X10Ext) pn.ext()).annotations(Annotationsopt);
                setResult(pn);
                break;
            }
     
            //
            // Rule 250:  SingleTypeImportDeclaration ::= import TypeName ;
            //
            case 250: {
                //#line 2192 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(2);
                //#line 2194 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Import(pos(getLeftSpan(), getRightSpan()), Import.CLASS, QName.make(TypeName.toString())));
                break;
            }
     
            //
            // Rule 251:  TypeImportOnDemandDeclaration ::= import PackageOrTypeName . * ;
            //
            case 251: {
                //#line 2198 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName PackageOrTypeName = (ParsedName) getRhsSym(2);
                //#line 2200 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Import(pos(getLeftSpan(), getRightSpan()), Import.PACKAGE, QName.make(PackageOrTypeName.toString())));
                break;
            }
     
            //
            // Rule 255:  TypeDeclaration ::= ;
            //
            case 255: {
                
                //#line 2215 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(null);
                break;
            }
     
            //
            // Rule 256:  ClassModifiers ::= ClassModifier
            //
            case 256: {
                //#line 2221 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassModifier = (List) getRhsSym(1);
                //#line 2223 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(ClassModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 257:  ClassModifiers ::= ClassModifiers ClassModifier
            //
            case 257: {
                //#line 2228 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassModifiers = (List) getRhsSym(1);
                //#line 2228 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassModifier = (List) getRhsSym(2);
                //#line 2230 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ClassModifiers.addAll(ClassModifier);
                break;
            }
     
            //
            // Rule 258:  ClassModifier ::= Annotation
            //
            case 258: {
                //#line 2234 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2236 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 259:  ClassModifier ::= public
            //
            case 259: {
                
                //#line 2241 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 260:  ClassModifier ::= protected
            //
            case 260: {
                
                //#line 2246 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 261:  ClassModifier ::= private
            //
            case 261: {
                
                //#line 2251 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 262:  ClassModifier ::= abstract
            //
            case 262: {
                
                //#line 2256 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.ABSTRACT)));
                break;
            }
     
            //
            // Rule 263:  ClassModifier ::= static
            //
            case 263: {
                
                //#line 2261 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STATIC)));
                break;
            }
     
            //
            // Rule 264:  ClassModifier ::= final
            //
            case 264: {
                
                //#line 2266 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 265:  ClassModifier ::= strictfp
            //
            case 265: {
                
                //#line 2271 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STRICTFP)));
                break;
            }
     
            //
            // Rule 266:  ClassModifier ::= safe
            //
            case 266: {
                
                //#line 2276 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.SAFE)));
                break;
            }
     
            //
            // Rule 267:  ClassModifier ::= value
            //
            case 267: {
                
                //#line 2281 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.VALUE)));
                break;
            }
     
            //
            // Rule 268:  TypeDefModifiers ::= TypeDefModifier
            //
            case 268: {
                //#line 2285 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDefModifier = (List) getRhsSym(1);
                //#line 2287 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(TypeDefModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 269:  TypeDefModifiers ::= TypeDefModifiers TypeDefModifier
            //
            case 269: {
                //#line 2292 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDefModifiers = (List) getRhsSym(1);
                //#line 2292 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeDefModifier = (List) getRhsSym(2);
                //#line 2294 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                TypeDefModifiers.addAll(TypeDefModifier);
                break;
            }
     
            //
            // Rule 270:  TypeDefModifier ::= Annotation
            //
            case 270: {
                //#line 2298 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2300 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 271:  TypeDefModifier ::= public
            //
            case 271: {
                
                //#line 2305 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 272:  TypeDefModifier ::= protected
            //
            case 272: {
                
                //#line 2310 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 273:  TypeDefModifier ::= private
            //
            case 273: {
                
                //#line 2315 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 274:  TypeDefModifier ::= abstract
            //
            case 274: {
                
                //#line 2320 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.ABSTRACT)));
                break;
            }
     
            //
            // Rule 275:  TypeDefModifier ::= static
            //
            case 275: {
                
                //#line 2325 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STATIC)));
                break;
            }
     
            //
            // Rule 276:  TypeDefModifier ::= final
            //
            case 276: {
                
                //#line 2330 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 277:  Interfaces ::= implements InterfaceTypeList
            //
            case 277: {
                //#line 2337 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceTypeList = (List) getRhsSym(2);
                //#line 2339 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(InterfaceTypeList);
                break;
            }
     
            //
            // Rule 278:  InterfaceTypeList ::= Type
            //
            case 278: {
                //#line 2343 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(1);
                //#line 2345 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TypeNode.class, false);
                l.add(Type);
                setResult(l);
                break;
            }
     
            //
            // Rule 279:  InterfaceTypeList ::= InterfaceTypeList , Type
            //
            case 279: {
                //#line 2350 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceTypeList = (List) getRhsSym(1);
                //#line 2350 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 2352 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                InterfaceTypeList.add(Type);
                setResult(InterfaceTypeList);
                break;
            }
     
            //
            // Rule 280:  ClassBody ::= { ClassBodyDeclarationsopt }
            //
            case 280: {
                //#line 2360 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassBodyDeclarationsopt = (List) getRhsSym(2);
                //#line 2362 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.ClassBody(pos(getLeftSpan(), getRightSpan()), ClassBodyDeclarationsopt));
                break;
            }
     
            //
            // Rule 282:  ClassBodyDeclarations ::= ClassBodyDeclarations ClassBodyDeclaration
            //
            case 282: {
                //#line 2367 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassBodyDeclarations = (List) getRhsSym(1);
                //#line 2367 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ClassBodyDeclaration = (List) getRhsSym(2);
                //#line 2369 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ClassBodyDeclarations.addAll(ClassBodyDeclaration);
                // setResult(a);
                break;
            }
     
            //
            // Rule 284:  ClassBodyDeclaration ::= InstanceInitializer
            //
            case 284: {
                //#line 2375 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Initializer InstanceInitializer = (Initializer) getRhsSym(1);
                //#line 2377 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(InstanceInitializer);
                setResult(l);
                break;
            }
     
            //
            // Rule 285:  ClassBodyDeclaration ::= StaticInitializer
            //
            case 285: {
                //#line 2382 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Initializer StaticInitializer = (Initializer) getRhsSym(1);
                //#line 2384 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(StaticInitializer);
                setResult(l);
                break;
            }
     
            //
            // Rule 286:  ClassBodyDeclaration ::= ConstructorDeclaration
            //
            case 286: {
                //#line 2389 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ConstructorDecl ConstructorDeclaration = (ConstructorDecl) getRhsSym(1);
                //#line 2391 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(ConstructorDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 288:  ClassMemberDeclaration ::= MethodDeclaration
            //
            case 288: {
                //#line 2398 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassMember MethodDeclaration = (ClassMember) getRhsSym(1);
                //#line 2400 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(MethodDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 289:  ClassMemberDeclaration ::= PropertyMethodDeclaration
            //
            case 289: {
                //#line 2405 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassMember PropertyMethodDeclaration = (ClassMember) getRhsSym(1);
                //#line 2407 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(PropertyMethodDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 290:  ClassMemberDeclaration ::= TypeDefDeclaration
            //
            case 290: {
                //#line 2412 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeDecl TypeDefDeclaration = (TypeDecl) getRhsSym(1);
                //#line 2414 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(TypeDefDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 291:  ClassMemberDeclaration ::= ClassDeclaration
            //
            case 291: {
                //#line 2419 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassDecl ClassDeclaration = (ClassDecl) getRhsSym(1);
                //#line 2421 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(ClassDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 292:  ClassMemberDeclaration ::= InterfaceDeclaration
            //
            case 292: {
                //#line 2426 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassDecl InterfaceDeclaration = (ClassDecl) getRhsSym(1);
                //#line 2428 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(InterfaceDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 293:  ClassMemberDeclaration ::= ;
            //
            case 293: {
                
                //#line 2435 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                setResult(l);
                break;
            }
     
            //
            // Rule 294:  FormalDeclarators ::= FormalDeclarator
            //
            case 294: {
                //#line 2440 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FormalDeclarator = (Object[]) getRhsSym(1);
                //#line 2442 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Object[].class, false);
                l.add(FormalDeclarator);
                setResult(l);
                break;
            }
     
            //
            // Rule 295:  FormalDeclarators ::= FormalDeclarators , FormalDeclarator
            //
            case 295: {
                //#line 2447 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalDeclarators = (List) getRhsSym(1);
                //#line 2447 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FormalDeclarator = (Object[]) getRhsSym(3);
                //#line 2449 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FormalDeclarators.add(FormalDeclarator);
                break;
            }
     
            //
            // Rule 296:  FieldDeclarators ::= FieldDeclarator
            //
            case 296: {
                //#line 2454 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FieldDeclarator = (Object[]) getRhsSym(1);
                //#line 2456 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Object[].class, false);
                l.add(FieldDeclarator);
                setResult(l);
                break;
            }
     
            //
            // Rule 297:  FieldDeclarators ::= FieldDeclarators , FieldDeclarator
            //
            case 297: {
                //#line 2461 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldDeclarators = (List) getRhsSym(1);
                //#line 2461 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FieldDeclarator = (Object[]) getRhsSym(3);
                //#line 2463 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FieldDeclarators.add(FieldDeclarator);
                // setResult(FieldDeclarators);
                break;
            }
     
            //
            // Rule 298:  VariableDeclaratorsWithType ::= VariableDeclaratorWithType
            //
            case 298: {
                //#line 2469 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] VariableDeclaratorWithType = (Object[]) getRhsSym(1);
                //#line 2471 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Object[].class, false);
                l.add(VariableDeclaratorWithType);
                setResult(l);
                break;
            }
     
            //
            // Rule 299:  VariableDeclaratorsWithType ::= VariableDeclaratorsWithType , VariableDeclaratorWithType
            //
            case 299: {
                //#line 2476 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableDeclaratorsWithType = (List) getRhsSym(1);
                //#line 2476 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] VariableDeclaratorWithType = (Object[]) getRhsSym(3);
                //#line 2478 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                VariableDeclaratorsWithType.add(VariableDeclaratorWithType);
                // setResult(VariableDeclaratorsWithType);
                break;
            }
     
            //
            // Rule 300:  VariableDeclarators ::= VariableDeclarator
            //
            case 300: {
                //#line 2483 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] VariableDeclarator = (Object[]) getRhsSym(1);
                //#line 2485 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Object[].class, false);
                l.add(VariableDeclarator);
                setResult(l);
                break;
            }
     
            //
            // Rule 301:  VariableDeclarators ::= VariableDeclarators , VariableDeclarator
            //
            case 301: {
                //#line 2490 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableDeclarators = (List) getRhsSym(1);
                //#line 2490 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] VariableDeclarator = (Object[]) getRhsSym(3);
                //#line 2492 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                VariableDeclarators.add(VariableDeclarator);
                // setResult(VariableDeclarators);
                break;
            }
     
            //
            // Rule 303:  FieldModifiers ::= FieldModifier
            //
            case 303: {
                //#line 2499 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldModifier = (List) getRhsSym(1);
                //#line 2501 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(FieldModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 304:  FieldModifiers ::= FieldModifiers FieldModifier
            //
            case 304: {
                //#line 2506 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldModifiers = (List) getRhsSym(1);
                //#line 2506 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldModifier = (List) getRhsSym(2);
                //#line 2508 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FieldModifiers.addAll(FieldModifier);
                break;
            }
     
            //
            // Rule 305:  FieldModifier ::= Annotation
            //
            case 305: {
                //#line 2512 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2514 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 306:  FieldModifier ::= public
            //
            case 306: {
                
                //#line 2519 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 307:  FieldModifier ::= protected
            //
            case 307: {
                
                //#line 2524 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 308:  FieldModifier ::= private
            //
            case 308: {
                
                //#line 2529 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 309:  FieldModifier ::= static
            //
            case 309: {
                
                //#line 2534 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STATIC)));
                break;
            }
     
            //
            // Rule 310:  FieldModifier ::= transient
            //
            case 310: {
                
                //#line 2539 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.TRANSIENT)));
                break;
            }
     
            //
            // Rule 311:  FieldModifier ::= volatile
            //
            case 311: {
                
                //#line 2544 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.VOLATILE)));
                break;
            }
     
            //
            // Rule 312:  ResultType ::= : Type
            //
            case 312: {
                //#line 2548 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(2);
                //#line 2550 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Type);
                break;
            }
     
            //
            // Rule 313:  FormalParameters ::= ( FormalParameterList )
            //
            case 313: {
                //#line 2554 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterList = (List) getRhsSym(2);
                //#line 2556 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(FormalParameterList);
                break;
            }
     
            //
            // Rule 314:  FormalParameterList ::= FormalParameter
            //
            case 314: {
                //#line 2560 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal FormalParameter = (X10Formal) getRhsSym(1);
                //#line 2562 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Formal.class, false);
                l.add(FormalParameter);
                setResult(l);
                break;
            }
     
            //
            // Rule 315:  FormalParameterList ::= FormalParameterList , FormalParameter
            //
            case 315: {
                //#line 2567 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterList = (List) getRhsSym(1);
                //#line 2567 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                X10Formal FormalParameter = (X10Formal) getRhsSym(3);
                //#line 2569 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FormalParameterList.add(FormalParameter);
                break;
            }
     
            //
            // Rule 316:  LoopIndexDeclarator ::= Identifier ResultTypeopt
            //
            case 316: {
                //#line 2573 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2573 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(2);
                //#line 2575 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, null, ResultTypeopt, null });
                break;
            }
     
            //
            // Rule 317:  LoopIndexDeclarator ::= ( IdentifierList ) ResultTypeopt
            //
            case 317: {
                //#line 2578 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(2);
                //#line 2578 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(4);
                //#line 2580 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), null, IdentifierList, null, ResultTypeopt, null });
                break;
            }
     
            //
            // Rule 318:  LoopIndexDeclarator ::= Identifier ( IdentifierList ) ResultTypeopt
            //
            case 318: {
                //#line 2583 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2583 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(3);
                //#line 2583 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(5);
                //#line 2585 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, IdentifierList, null, ResultTypeopt, null });
                break;
            }
     
            //
            // Rule 319:  LoopIndex ::= VariableModifiersopt LoopIndexDeclarator
            //
            case 319: {
                //#line 2589 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 2589 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] LoopIndexDeclarator = (Object[]) getRhsSym(2);
                //#line 2591 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            Formal f;
                        	FlagsNode fn = extractFlags(VariableModifiersopt, Flags.FINAL);
            Object[] o = LoopIndexDeclarator;
            Position pos = (Position) o[0];
            Id name = (Id) o[1];
            if (name == null) name = nf.Id(pos, Name.makeFresh());
               List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                        if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        List explodedFormals = new ArrayList();
                        for (Iterator i = exploded.iterator(); i.hasNext(); ) {
                        	Id id = (Id) i.next();
                        	explodedFormals.add(nf.Formal(id.position(), fn, nf.UnknownTypeNode(id.position()), id));
                        }
            f = nf.X10Formal(pos(), fn, type, name, explodedFormals);
            f = (Formal) ((X10Ext) f.ext()).annotations(extractAnnotations(VariableModifiersopt));
            setResult(f);
                break;
            }
     
            //
            // Rule 320:  LoopIndex ::= VariableModifiersopt VarKeyword LoopIndexDeclarator
            //
            case 320: {
                //#line 2611 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 2611 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VarKeyword = (List) getRhsSym(2);
                //#line 2611 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] LoopIndexDeclarator = (Object[]) getRhsSym(3);
                //#line 2613 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            Formal f;
                        	FlagsNode fn = extractFlags(VariableModifiersopt, VarKeyword);
            Object[] o = LoopIndexDeclarator;
            Position pos = (Position) o[0];
            Id name = (Id) o[1];
                            if (name == null) name = nf.Id(pos, Name.makeFresh());
               List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                                                    if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                                                    List explodedFormals = new ArrayList();
                        for (Iterator i = exploded.iterator(); i.hasNext(); ) {
                        	Id id = (Id) i.next();
                        	explodedFormals.add(nf.Formal(id.position(), fn, nf.UnknownTypeNode(id.position()), id));
                        }
            f = nf.X10Formal(pos(), fn, type, name, explodedFormals);
            f = (Formal) ((X10Ext) f.ext()).annotations(extractAnnotations(VariableModifiersopt));
            setResult(f);
                break;
            }
     
            //
            // Rule 321:  FormalParameter ::= VariableModifiersopt FormalDeclarator
            //
            case 321: {
                //#line 2634 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 2634 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FormalDeclarator = (Object[]) getRhsSym(2);
                //#line 2636 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            Formal f;
                        	FlagsNode fn = extractFlags(VariableModifiersopt, Flags.FINAL);
            Object[] o = FormalDeclarator;
            Position pos = (Position) o[0];
            Id name = (Id) o[1];
                            if (name == null) name = nf.Id(pos, Name.makeFresh());
               List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                        if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        Expr init = (Expr) o[5];
                        List explodedFormals = new ArrayList();
                        for (Iterator i = exploded.iterator(); i.hasNext(); ) {
                        	Id id = (Id) i.next();
                        	explodedFormals.add(nf.Formal(id.position(), fn, nf.UnknownTypeNode(id.position()), id));
                        }
            f = nf.X10Formal(pos(), fn, type, name, explodedFormals);
            f = (Formal) ((X10Ext) f.ext()).annotations(extractAnnotations(VariableModifiersopt));
            setResult(f);
                break;
            }
     
            //
            // Rule 322:  FormalParameter ::= VariableModifiersopt VarKeyword FormalDeclarator
            //
            case 322: {
                //#line 2657 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 2657 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VarKeyword = (List) getRhsSym(2);
                //#line 2657 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object[] FormalDeclarator = (Object[]) getRhsSym(3);
                //#line 2659 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            Formal f;
                        	FlagsNode fn = extractFlags(VariableModifiersopt, VarKeyword);
            Object[] o = FormalDeclarator;
            Position pos = (Position) o[0];
            Id name = (Id) o[1];
                            if (name == null) name = nf.Id(pos, Name.makeFresh());
               List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                                                    if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        Expr init = (Expr) o[5];
                                                    List explodedFormals = new ArrayList();
                        for (Iterator i = exploded.iterator(); i.hasNext(); ) {
                        	Id id = (Id) i.next();
                        	explodedFormals.add(nf.Formal(id.position(), fn, nf.UnknownTypeNode(id.position()), id));
                        }
            f = nf.X10Formal(pos(), fn, type, name, explodedFormals);
            f = (Formal) ((X10Ext) f.ext()).annotations(extractAnnotations(VariableModifiersopt));
            setResult(f);
                break;
            }
     
            //
            // Rule 323:  FormalParameter ::= Type
            //
            case 323: {
                //#line 2680 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(1);
                //#line 2682 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
            Formal f;
            f = nf.X10Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), Type, nf.Id(pos(), Name.makeFresh()), Collections.EMPTY_LIST);
            setResult(f);
                break;
            }
     
            //
            // Rule 324:  VariableModifiers ::= VariableModifier
            //
            case 324: {
                //#line 2688 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifier = (List) getRhsSym(1);
                //#line 2690 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(VariableModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 325:  VariableModifiers ::= VariableModifiers VariableModifier
            //
            case 325: {
                //#line 2695 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiers = (List) getRhsSym(1);
                //#line 2695 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifier = (List) getRhsSym(2);
                //#line 2697 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                VariableModifiers.addAll(VariableModifier);
                break;
            }
     
            //
            // Rule 326:  VariableModifier ::= Annotation
            //
            case 326: {
                //#line 2701 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2703 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 327:  VariableModifier ::= shared
            //
            case 327: {
                
                //#line 2708 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.SHARED)));
                break;
            }
     
            //
            // Rule 328:  MethodModifiers ::= MethodModifier
            //
            case 328: {
                //#line 2715 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifier = (List) getRhsSym(1);
                //#line 2717 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(MethodModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 329:  MethodModifiers ::= MethodModifiers MethodModifier
            //
            case 329: {
                //#line 2722 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifiers = (List) getRhsSym(1);
                //#line 2722 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List MethodModifier = (List) getRhsSym(2);
                //#line 2724 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                MethodModifiers.addAll(MethodModifier);
                break;
            }
     
            //
            // Rule 330:  MethodModifier ::= Annotation
            //
            case 330: {
                //#line 2728 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2730 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 331:  MethodModifier ::= public
            //
            case 331: {
                
                //#line 2735 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 332:  MethodModifier ::= protected
            //
            case 332: {
                
                //#line 2740 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 333:  MethodModifier ::= private
            //
            case 333: {
                
                //#line 2745 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 334:  MethodModifier ::= abstract
            //
            case 334: {
                
                //#line 2750 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.ABSTRACT)));
                break;
            }
     
            //
            // Rule 335:  MethodModifier ::= static
            //
            case 335: {
                
                //#line 2755 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STATIC)));
                break;
            }
     
            //
            // Rule 336:  MethodModifier ::= final
            //
            case 336: {
                
                //#line 2760 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.FINAL)));
                break;
            }
     
            //
            // Rule 337:  MethodModifier ::= native
            //
            case 337: {
                
                //#line 2765 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.NATIVE)));
                break;
            }
     
            //
            // Rule 338:  MethodModifier ::= strictfp
            //
            case 338: {
                
                //#line 2770 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STRICTFP)));
                break;
            }
     
            //
            // Rule 339:  MethodModifier ::= atomic
            //
            case 339: {
                
                //#line 2775 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.ATOMIC)));
                break;
            }
     
            //
            // Rule 340:  MethodModifier ::= extern
            //
            case 340: {
                
                //#line 2780 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.EXTERN)));
                break;
            }
     
            //
            // Rule 341:  MethodModifier ::= safe
            //
            case 341: {
                
                //#line 2785 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.SAFE)));
                break;
            }
     
            //
            // Rule 342:  MethodModifier ::= sequential
            //
            case 342: {
                
                //#line 2790 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.SEQUENTIAL)));
                break;
            }
     
            //
            // Rule 343:  MethodModifier ::= local
            //
            case 343: {
                
                //#line 2795 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.LOCAL)));
                break;
            }
     
            //
            // Rule 344:  MethodModifier ::= nonblocking
            //
            case 344: {
                
                //#line 2800 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.NON_BLOCKING)));
                break;
            }
     
            //
            // Rule 345:  MethodModifier ::= incomplete
            //
            case 345: {
                
                //#line 2805 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.INCOMPLETE)));
                break;
            }
     
            //
            // Rule 346:  MethodModifier ::= property
            //
            case 346: {
                
                //#line 2810 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), X10Flags.PROPERTY)));
                break;
            }
     
            //
            // Rule 347:  Throws ::= throws ExceptionTypeList
            //
            case 347: {
                //#line 2815 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExceptionTypeList = (List) getRhsSym(2);
                //#line 2817 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ExceptionTypeList);
                break;
            }
     
            //
            // Rule 348:  ExceptionTypeList ::= ExceptionType
            //
            case 348: {
                //#line 2821 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ExceptionType = (TypeNode) getRhsSym(1);
                //#line 2823 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TypeNode.class, false);
                l.add(ExceptionType);
                setResult(l);
                break;
            }
     
            //
            // Rule 349:  ExceptionTypeList ::= ExceptionTypeList , ExceptionType
            //
            case 349: {
                //#line 2828 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExceptionTypeList = (List) getRhsSym(1);
                //#line 2828 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ExceptionType = (TypeNode) getRhsSym(3);
                //#line 2830 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ExceptionTypeList.add(ExceptionType);
                break;
            }
     
            //
            // Rule 351:  MethodBody ::= = LastExpression ;
            //
            case 351: {
                //#line 2836 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt LastExpression = (Stmt) getRhsSym(2);
                //#line 2838 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Block(pos(), LastExpression));
                break;
            }
     
            //
            // Rule 352:  MethodBody ::= = { BlockStatementsopt LastExpression }
            //
            case 352: {
                //#line 2841 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatementsopt = (List) getRhsSym(3);
                //#line 2841 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt LastExpression = (Stmt) getRhsSym(4);
                //#line 2843 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new ArrayList();
                l.addAll(BlockStatementsopt);
                l.add(LastExpression);
                setResult(nf.Block(pos(), l));
                break;
            }
     
            //
            // Rule 353:  MethodBody ::= = Block
            //
            case 353: {
                //#line 2849 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(2);
                //#line 2851 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Block);
                break;
            }
     
            //
            // Rule 354:  MethodBody ::= Block
            //
            case 354: {
                //#line 2854 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(1);
                //#line 2856 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Block);
                break;
            }
     
            //
            // Rule 355:  MethodBody ::= ;
            //
            case 355:
                setResult(null);
                break;
 
            //
            // Rule 356:  InstanceInitializer ::= Block
            //
            case 356: {
                //#line 2862 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(1);
                //#line 2864 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Initializer(pos(), nf.FlagsNode(pos(), Flags.NONE), Block));
                break;
            }
     
            //
            // Rule 357:  StaticInitializer ::= static Block
            //
            case 357: {
                //#line 2868 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block Block = (Block) getRhsSym(2);
                //#line 2870 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Initializer(pos(), nf.FlagsNode(pos(getLeftSpan()), Flags.STATIC), Block));
                break;
            }
     
            //
            // Rule 358:  SimpleTypeName ::= Identifier
            //
            case 358: {
                //#line 2874 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 2876 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 359:  ConstructorModifiers ::= ConstructorModifier
            //
            case 359: {
                //#line 2880 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ConstructorModifier = (List) getRhsSym(1);
                //#line 2882 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(ConstructorModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 360:  ConstructorModifiers ::= ConstructorModifiers ConstructorModifier
            //
            case 360: {
                //#line 2887 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ConstructorModifiers = (List) getRhsSym(1);
                //#line 2887 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ConstructorModifier = (List) getRhsSym(2);
                //#line 2889 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ConstructorModifiers.addAll(ConstructorModifier);
                break;
            }
     
            //
            // Rule 361:  ConstructorModifier ::= Annotation
            //
            case 361: {
                //#line 2893 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2895 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 362:  ConstructorModifier ::= public
            //
            case 362: {
                
                //#line 2900 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 363:  ConstructorModifier ::= protected
            //
            case 363: {
                
                //#line 2905 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 364:  ConstructorModifier ::= private
            //
            case 364: {
                
                //#line 2910 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 365:  ConstructorModifier ::= native
            //
            case 365: {
                
                //#line 2915 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.NATIVE)));
                break;
            }
     
            //
            // Rule 366:  ConstructorBody ::= = ConstructorBlock
            //
            case 366: {
                //#line 2919 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ConstructorBlock = (Block) getRhsSym(2);
                //#line 2921 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ConstructorBlock);
                break;
            }
     
            //
            // Rule 367:  ConstructorBody ::= ConstructorBlock
            //
            case 367: {
                //#line 2924 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Block ConstructorBlock = (Block) getRhsSym(1);
                //#line 2926 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ConstructorBlock);
                break;
            }
     
            //
            // Rule 368:  ConstructorBody ::= = ExplicitConstructorInvocation
            //
            case 368: {
                //#line 2929 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ConstructorCall ExplicitConstructorInvocation = (ConstructorCall) getRhsSym(2);
                //#line 2931 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l;
                l = new TypedList(new LinkedList(), Stmt.class, false);
                l.add(ExplicitConstructorInvocation);
                setResult(nf.Block(pos(), l));
                break;
            }
     
            //
            // Rule 369:  ConstructorBody ::= = AssignPropertyCall
            //
            case 369: {
                //#line 2937 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt AssignPropertyCall = (Stmt) getRhsSym(2);
                //#line 2939 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l;
                l = new TypedList(new LinkedList(), Stmt.class, false);
                l.add(AssignPropertyCall);
                setResult(nf.Block(pos(), l));
                break;
            }
     
            //
            // Rule 370:  ConstructorBody ::= ;
            //
            case 370:
                setResult(null);
                break;
 
            //
            // Rule 371:  ConstructorBlock ::= { ExplicitConstructorInvocationopt BlockStatementsopt }
            //
            case 371: {
                //#line 2948 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt ExplicitConstructorInvocationopt = (Stmt) getRhsSym(2);
                //#line 2948 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatementsopt = (List) getRhsSym(3);
                //#line 2950 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l;
                l = new TypedList(new LinkedList(), Stmt.class, false);
                if (ExplicitConstructorInvocationopt == null)
                {
                    l.add(nf.SuperCall(pos(), Collections.EMPTY_LIST));
                }
                else
                {
                    l.add(ExplicitConstructorInvocationopt);
                }
                l.addAll(BlockStatementsopt);
                setResult(nf.Block(pos(), l));
                break;
            }
     
            //
            // Rule 372:  Arguments ::= ( ArgumentListopt )
            //
            case 372: {
                //#line 2965 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(2);
                //#line 2967 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ArgumentListopt);
                break;
            }
     
            //
            // Rule 374:  InterfaceModifiers ::= InterfaceModifier
            //
            case 374: {
                //#line 2975 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceModifier = (List) getRhsSym(1);
                //#line 2977 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new LinkedList();
                l.addAll(InterfaceModifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 375:  InterfaceModifiers ::= InterfaceModifiers InterfaceModifier
            //
            case 375: {
                //#line 2982 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceModifiers = (List) getRhsSym(1);
                //#line 2982 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceModifier = (List) getRhsSym(2);
                //#line 2984 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                InterfaceModifiers.addAll(InterfaceModifier);
                break;
            }
     
            //
            // Rule 376:  InterfaceModifier ::= Annotation
            //
            case 376: {
                //#line 2988 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 2990 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(Annotation));
                break;
            }
     
            //
            // Rule 377:  InterfaceModifier ::= public
            //
            case 377: {
                
                //#line 2995 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PUBLIC)));
                break;
            }
     
            //
            // Rule 378:  InterfaceModifier ::= protected
            //
            case 378: {
                
                //#line 3000 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PROTECTED)));
                break;
            }
     
            //
            // Rule 379:  InterfaceModifier ::= private
            //
            case 379: {
                
                //#line 3005 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.PRIVATE)));
                break;
            }
     
            //
            // Rule 380:  InterfaceModifier ::= abstract
            //
            case 380: {
                
                //#line 3010 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.ABSTRACT)));
                break;
            }
     
            //
            // Rule 381:  InterfaceModifier ::= static
            //
            case 381: {
                
                //#line 3015 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STATIC)));
                break;
            }
     
            //
            // Rule 382:  InterfaceModifier ::= strictfp
            //
            case 382: {
                
                //#line 3020 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.singletonList(nf.FlagsNode(pos(), Flags.STRICTFP)));
                break;
            }
     
            //
            // Rule 383:  ExtendsInterfaces ::= extends Type
            //
            case 383: {
                //#line 3024 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(2);
                //#line 3026 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), TypeNode.class, false);
                l.add(Type);
                setResult(l);
                break;
            }
     
            //
            // Rule 384:  ExtendsInterfaces ::= ExtendsInterfaces , Type
            //
            case 384: {
                //#line 3031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ExtendsInterfaces = (List) getRhsSym(1);
                //#line 3031 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 3033 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ExtendsInterfaces.add(Type);
                break;
            }
     
            //
            // Rule 385:  InterfaceBody ::= { InterfaceMemberDeclarationsopt }
            //
            case 385: {
                //#line 3040 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceMemberDeclarationsopt = (List) getRhsSym(2);
                //#line 3042 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.ClassBody(pos(), InterfaceMemberDeclarationsopt));
                break;
            }
     
            //
            // Rule 387:  InterfaceMemberDeclarations ::= InterfaceMemberDeclarations InterfaceMemberDeclaration
            //
            case 387: {
                //#line 3047 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceMemberDeclarations = (List) getRhsSym(1);
                //#line 3047 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List InterfaceMemberDeclaration = (List) getRhsSym(2);
                //#line 3049 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                InterfaceMemberDeclarations.addAll(InterfaceMemberDeclaration);
                // setResult(l);
                break;
            }
     
            //
            // Rule 388:  InterfaceMemberDeclaration ::= MethodDeclaration
            //
            case 388: {
                //#line 3054 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassMember MethodDeclaration = (ClassMember) getRhsSym(1);
                //#line 3056 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(MethodDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 389:  InterfaceMemberDeclaration ::= PropertyMethodDeclaration
            //
            case 389: {
                //#line 3061 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassMember PropertyMethodDeclaration = (ClassMember) getRhsSym(1);
                //#line 3063 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(PropertyMethodDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 390:  InterfaceMemberDeclaration ::= FieldDeclaration
            //
            case 390: {
                //#line 3068 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FieldDeclaration = (List) getRhsSym(1);
                //#line 3070 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.addAll(FieldDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 391:  InterfaceMemberDeclaration ::= ClassDeclaration
            //
            case 391: {
                //#line 3075 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassDecl ClassDeclaration = (ClassDecl) getRhsSym(1);
                //#line 3077 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(ClassDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 392:  InterfaceMemberDeclaration ::= InterfaceDeclaration
            //
            case 392: {
                //#line 3082 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassDecl InterfaceDeclaration = (ClassDecl) getRhsSym(1);
                //#line 3084 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(InterfaceDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 393:  InterfaceMemberDeclaration ::= TypeDefDeclaration
            //
            case 393: {
                //#line 3089 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeDecl TypeDefDeclaration = (TypeDecl) getRhsSym(1);
                //#line 3091 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), ClassMember.class, false);
                l.add(TypeDefDeclaration);
                setResult(l);
                break;
            }
     
            //
            // Rule 394:  InterfaceMemberDeclaration ::= ;
            //
            case 394: {
                
                //#line 3098 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 395:  Annotations ::= Annotation
            //
            case 395: {
                //#line 3102 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(1);
                //#line 3104 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), AnnotationNode.class, false);
                l.add(Annotation);
                setResult(l);
                break;
            }
     
            //
            // Rule 396:  Annotations ::= Annotations Annotation
            //
            case 396: {
                //#line 3109 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotations = (List) getRhsSym(1);
                //#line 3109 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                AnnotationNode Annotation = (AnnotationNode) getRhsSym(2);
                //#line 3111 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Annotations.add(Annotation);
                break;
            }
     
            //
            // Rule 397:  Annotation ::= @ NamedType
            //
            case 397: {
                //#line 3115 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode NamedType = (TypeNode) getRhsSym(2);
                //#line 3117 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.AnnotationNode(pos(), NamedType));
                break;
            }
     
            //
            // Rule 398:  SimpleName ::= Identifier
            //
            case 398: {
                //#line 3121 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3123 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new X10ParsedName(nf, ts, pos(), Identifier));
                break;
            }
     
            //
            // Rule 399:  Identifier ::= identifier
            //
            case 399: {
                //#line 3127 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                polyglot.lex.Identifier identifier = (polyglot.lex.Identifier) getRhsSym(1);
                //#line 3129 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult( nf.Id(identifier.getPosition(), identifier.getIdentifier()));
                break;
            }
     
            //
            // Rule 400:  ArrayInitializer ::= [ VariableInitializersopt ,opt$opt ]
            //
            case 400: {
                //#line 3135 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableInitializersopt = (List) getRhsSym(2);
                //#line 3135 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Object opt = (Object) getRhsSym(3);
                //#line 3137 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (VariableInitializersopt == null)
                     setResult(nf.ArrayInit(pos()));
                else setResult(nf.ArrayInit(pos(), VariableInitializersopt));
                break;
            }
     
            //
            // Rule 401:  VariableInitializers ::= VariableInitializer
            //
            case 401: {
                //#line 3143 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(1);
                //#line 3145 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Expr.class, false);
                l.add(VariableInitializer);
                setResult(l);
                break;
            }
     
            //
            // Rule 402:  VariableInitializers ::= VariableInitializers , VariableInitializer
            //
            case 402: {
                //#line 3150 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableInitializers = (List) getRhsSym(1);
                //#line 3150 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(3);
                //#line 3152 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                VariableInitializers.add(VariableInitializer);
                //setResult(VariableInitializers);
                break;
            }
     
            //
            // Rule 403:  Block ::= { BlockStatementsopt }
            //
            case 403: {
                //#line 3168 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatementsopt = (List) getRhsSym(2);
                //#line 3170 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Block(pos(), BlockStatementsopt));
                break;
            }
     
            //
            // Rule 404:  BlockStatements ::= BlockStatement
            //
            case 404: {
                //#line 3174 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatement = (List) getRhsSym(1);
                //#line 3176 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Stmt.class, false);
                l.addAll(BlockStatement);
                setResult(l);
                break;
            }
     
            //
            // Rule 405:  BlockStatements ::= BlockStatements BlockStatement
            //
            case 405: {
                //#line 3181 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatements = (List) getRhsSym(1);
                //#line 3181 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List BlockStatement = (List) getRhsSym(2);
                //#line 3183 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                BlockStatements.addAll(BlockStatement);
                //setResult(l);
                break;
            }
     
            //
            // Rule 407:  BlockStatement ::= ClassDeclaration
            //
            case 407: {
                //#line 3189 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ClassDecl ClassDeclaration = (ClassDecl) getRhsSym(1);
                //#line 3191 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Stmt.class, false);
                l.add(nf.LocalClassDecl(pos(), ClassDeclaration));
                setResult(l);
                break;
            }
     
            //
            // Rule 408:  BlockStatement ::= TypeDefDeclaration
            //
            case 408: {
                //#line 3196 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeDecl TypeDefDeclaration = (TypeDecl) getRhsSym(1);
                //#line 3198 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Stmt.class, false);
                l.add(nf.LocalTypeDef(pos(), TypeDefDeclaration));
                setResult(l);
                break;
            }
     
            //
            // Rule 409:  BlockStatement ::= Statement
            //
            case 409: {
                //#line 3203 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Stmt Statement = (Stmt) getRhsSym(1);
                //#line 3205 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Stmt.class, false);
                l.add(Statement);
                setResult(l);
                break;
            }
     
            //
            // Rule 410:  IdentifierList ::= Identifier
            //
            case 410: {
                //#line 3211 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3213 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Id.class, false);
                l.add(Identifier);
                setResult(l);
                break;
            }
     
            //
            // Rule 411:  IdentifierList ::= IdentifierList , Identifier
            //
            case 411: {
                //#line 3218 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(1);
                //#line 3218 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3220 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                IdentifierList.add(Identifier);
                break;
            }
     
            //
            // Rule 412:  FormalDeclarator ::= Identifier : Type
            //
            case 412: {
                //#line 3224 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3224 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 3226 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, null, Type, null });
                break;
            }
     
            //
            // Rule 413:  FormalDeclarator ::= ( IdentifierList ) : Type
            //
            case 413: {
                //#line 3229 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(2);
                //#line 3229 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(5);
                //#line 3231 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), null, IdentifierList, null, Type, null });
                break;
            }
     
            //
            // Rule 414:  FormalDeclarator ::= Identifier ( IdentifierList ) : Type
            //
            case 414: {
                //#line 3234 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3234 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(3);
                //#line 3234 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(6);
                //#line 3236 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, IdentifierList, null, Type, null });
                break;
            }
     
            //
            // Rule 415:  FieldDeclarator ::= Identifier WhereClauseopt : Type
            //
            case 415: {
                //#line 3240 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3240 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(2);
                //#line 3240 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(4);
                //#line 3242 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, WhereClauseopt, Type, null });
                break;
            }
     
            //
            // Rule 416:  FieldDeclarator ::= Identifier WhereClauseopt ResultTypeopt = VariableInitializer
            //
            case 416: {
                //#line 3245 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3245 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                DepParameterExpr WhereClauseopt = (DepParameterExpr) getRhsSym(2);
                //#line 3245 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(3);
                //#line 3245 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(5);
                //#line 3247 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, WhereClauseopt, ResultTypeopt, VariableInitializer });
                break;
            }
     
            //
            // Rule 417:  VariableDeclarator ::= Identifier ResultTypeopt = VariableInitializer
            //
            case 417: {
                //#line 3251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(2);
                //#line 3251 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(4);
                //#line 3253 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, null, ResultTypeopt, VariableInitializer });
                break;
            }
     
            //
            // Rule 418:  VariableDeclarator ::= ( IdentifierList ) ResultTypeopt = VariableInitializer
            //
            case 418: {
                //#line 3256 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(2);
                //#line 3256 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(4);
                //#line 3256 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(6);
                //#line 3258 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), null, IdentifierList, null, ResultTypeopt, VariableInitializer });
                break;
            }
     
            //
            // Rule 419:  VariableDeclarator ::= Identifier ( IdentifierList ) ResultTypeopt = VariableInitializer
            //
            case 419: {
                //#line 3261 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3261 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(3);
                //#line 3261 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultTypeopt = (TypeNode) getRhsSym(5);
                //#line 3261 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(7);
                //#line 3263 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, IdentifierList, null, ResultTypeopt, VariableInitializer });
                break;
            }
     
            //
            // Rule 420:  VariableDeclaratorWithType ::= Identifier ResultType = VariableInitializer
            //
            case 420: {
                //#line 3267 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3267 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultType = (TypeNode) getRhsSym(2);
                //#line 3267 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(4);
                //#line 3269 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, Collections.EMPTY_LIST, null, ResultType, VariableInitializer });
                break;
            }
     
            //
            // Rule 421:  VariableDeclaratorWithType ::= ( IdentifierList ) ResultType = VariableInitializer
            //
            case 421: {
                //#line 3272 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(2);
                //#line 3272 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultType = (TypeNode) getRhsSym(4);
                //#line 3272 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(6);
                //#line 3274 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), null, IdentifierList, null, ResultType, VariableInitializer });
                break;
            }
     
            //
            // Rule 422:  VariableDeclaratorWithType ::= Identifier ( IdentifierList ) ResultType = VariableInitializer
            //
            case 422: {
                //#line 3277 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 3277 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List IdentifierList = (List) getRhsSym(3);
                //#line 3277 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode ResultType = (TypeNode) getRhsSym(5);
                //#line 3277 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr VariableInitializer = (Expr) getRhsSym(7);
                //#line 3279 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new Object[] { pos(), Identifier, IdentifierList, null, ResultType, VariableInitializer });
                break;
            }
     
            //
            // Rule 424:  LocalVariableDeclaration ::= VariableModifiersopt VarKeyword VariableDeclarators
            //
            case 424: {
                //#line 3285 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 3285 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VarKeyword = (List) getRhsSym(2);
                //#line 3285 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableDeclarators = (List) getRhsSym(3);
                //#line 3287 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = extractFlags(VariableModifiersopt, VarKeyword);
    
                List l = new TypedList(new LinkedList(), LocalDecl.class, false);
                List s = new TypedList(new LinkedList(), Stmt.class, false);
                    for (Iterator i = VariableDeclarators.iterator(); i.hasNext(); )
                    {
                        Object[] o = (Object[]) i.next();
                        Position pos = (Position) o[0];
                        Id name = (Id) o[1];
                        if (name == null) name = nf.Id(pos, Name.makeFresh());
                        List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                        if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        Expr init = (Expr) o[5];
                        LocalDecl ld = nf.LocalDecl(pos, fn,
                                           type, name, init);
                        ld = (LocalDecl) ((X10Ext) ld.ext()).annotations(extractAnnotations(VariableModifiersopt));
                        int index = 0;
                        l.add(ld);
                        for (Iterator j = exploded.iterator(); j.hasNext(); ) {
                        	Id id = (Id) j.next();
                        	TypeNode tni = nf.UnknownTypeNode(id.position());
                        	l.add(nf.LocalDecl(id.position(), fn, tni, id, init != null ? nf.ClosureCall(init.position(), nf.Local(init.position(), name), Collections.EMPTY_LIST, Collections.<Expr>singletonList(nf.IntLit(init.position(), IntLit.INT, index))) : null));
                        	index++;
                        }
                    }
                l.addAll(s); 
                setResult(l);
                break;
            }
     
            //
            // Rule 425:  LocalVariableDeclaration ::= VariableModifiersopt VariableDeclaratorsWithType
            //
            case 425: {
                //#line 3318 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 3318 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableDeclaratorsWithType = (List) getRhsSym(2);
                //#line 3320 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = extractFlags(VariableModifiersopt, Flags.FINAL);
    
                List l = new TypedList(new LinkedList(), LocalDecl.class, false);
                List s = new TypedList(new LinkedList(), Stmt.class, false);
                    for (Iterator i = VariableDeclaratorsWithType.iterator(); i.hasNext(); )
                    {
                        Object[] o = (Object[]) i.next();
                        Position pos = (Position) o[0];
                        Id name = (Id) o[1];
                        if (name == null) name = nf.Id(pos, Name.makeFresh());
                        List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                        if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        Expr init = (Expr) o[5];
                        LocalDecl ld = nf.LocalDecl(pos, fn,
                                           type, name, init);
                        ld = (LocalDecl) ((X10Ext) ld.ext()).annotations(extractAnnotations(VariableModifiersopt));
                        int index = 0;
                        l.add(ld);
                        for (Iterator j = exploded.iterator(); j.hasNext(); ) {
                        	Id id = (Id) j.next();
                        	// HACK: if the local is non-final, assume the type is point and the component is int
                        	TypeNode tni = nf.UnknownTypeNode(id.position());
                        	l.add(nf.LocalDecl(id.position(), fn, tni, id, init != null ? nf.ClosureCall(init.position(), nf.Local(init.position(), name), Collections.EMPTY_LIST, Collections.<Expr>singletonList(nf.IntLit(init.position(), IntLit.INT, index))) : null));
                        	index++;
                        }
                    }
                l.addAll(s); 
                setResult(l);
                break;
            }
     
            //
            // Rule 426:  LocalVariableDeclaration ::= VariableModifiersopt VarKeyword FormalDeclarators
            //
            case 426: {
                //#line 3352 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VariableModifiersopt = (List) getRhsSym(1);
                //#line 3352 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List VarKeyword = (List) getRhsSym(2);
                //#line 3352 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalDeclarators = (List) getRhsSym(3);
                //#line 3354 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                FlagsNode fn = extractFlags(VariableModifiersopt, VarKeyword);
    
                List l = new TypedList(new LinkedList(), LocalDecl.class, false);
                List s = new TypedList(new LinkedList(), Stmt.class, false);
                    for (Iterator i = FormalDeclarators.iterator(); i.hasNext(); )
                    {
                        Object[] o = (Object[]) i.next();
                        Position pos = (Position) o[0];
                        Id name = (Id) o[1];
                        if (name == null) name = nf.Id(pos, Name.makeFresh());
                        List exploded = (List) o[2];
                        DepParameterExpr guard = (DepParameterExpr) o[3];
                        TypeNode type = (TypeNode) o[4];
                                                    if (type == null) type = nf.UnknownTypeNode(name != null ? name.position() : pos);
                        Expr init = (Expr) o[5];
                        LocalDecl ld = nf.LocalDecl(pos, fn,
                                           type, name, init);
                        ld = (LocalDecl) ((X10Ext) ld.ext()).annotations(extractAnnotations(VariableModifiersopt));
                        int index = 0;
                        l.add(ld);
                        for (Iterator j = exploded.iterator(); j.hasNext(); ) {
                        	Id id = (Id) j.next();
                        	// HACK: if the local is non-final, assume the type is point and the component is int
                        	TypeNode tni = nf.UnknownTypeNode(id.position());
                        	l.add(nf.LocalDecl(id.position(), fn, tni, id, init != null ? nf.ClosureCall(init.position(), nf.Local(init.position(), name), Collections.EMPTY_LIST, Collections.<Expr>singletonList(nf.IntLit(init.position(), IntLit.INT, index))) : null));
                        	index++;
                        }
                    }
                l.addAll(s); 
                setResult(l);
                break;
            }
     
            //
            // Rule 428:  Primary ::= TypeName . class
            //
            case 428: {
                //#line 3393 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3395 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (TypeName instanceof ParsedName)
                {
                    ParsedName a = (ParsedName) TypeName;
                    setResult(nf.ClassLit(pos(), a.toType()));
                }
                else assert(false);
                break;
            }
     
            //
            // Rule 429:  Primary ::= self
            //
            case 429: {
                
                //#line 3405 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Self(pos()));
                break;
            }
     
            //
            // Rule 430:  Primary ::= this
            //
            case 430: {
                
                //#line 3410 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.This(pos()));
                break;
            }
     
            //
            // Rule 431:  Primary ::= ClassName . this
            //
            case 431: {
                //#line 3413 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 3415 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.This(pos(), ClassName.toType()));
                break;
            }
     
            //
            // Rule 432:  Primary ::= ( Expression )
            //
            case 432: {
                //#line 3418 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(2);
                //#line 3420 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.ParExpr(pos(), Expression));
                break;
            }
     
            //
            // Rule 438:  OperatorFunction ::= TypeName . +
            //
            case 438: {
                //#line 3429 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3431 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.ADD, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 439:  OperatorFunction ::= TypeName . -
            //
            case 439: {
                //#line 3440 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3442 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.SUB, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 440:  OperatorFunction ::= TypeName . *
            //
            case 440: {
                //#line 3451 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3453 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.MUL, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 441:  OperatorFunction ::= TypeName . /
            //
            case 441: {
                //#line 3462 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3464 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.DIV, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 442:  OperatorFunction ::= TypeName . %
            //
            case 442: {
                //#line 3473 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3475 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.MOD, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 443:  OperatorFunction ::= TypeName . &
            //
            case 443: {
                //#line 3484 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3486 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.BIT_AND, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 444:  OperatorFunction ::= TypeName . |
            //
            case 444: {
                //#line 3495 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3497 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.BIT_OR, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 445:  OperatorFunction ::= TypeName . ^
            //
            case 445: {
                //#line 3506 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3508 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.BIT_XOR, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 446:  OperatorFunction ::= TypeName . <<
            //
            case 446: {
                //#line 3517 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3519 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.SHL, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 447:  OperatorFunction ::= TypeName . >>
            //
            case 447: {
                //#line 3528 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3530 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.SHR, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 448:  OperatorFunction ::= TypeName . >>>
            //
            case 448: {
                //#line 3539 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3541 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.USHR, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 449:  OperatorFunction ::= TypeName . <
            //
            case 449: {
                //#line 3550 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3552 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.LT, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 450:  OperatorFunction ::= TypeName . <=
            //
            case 450: {
                //#line 3561 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3563 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.LE, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 451:  OperatorFunction ::= TypeName . >=
            //
            case 451: {
                //#line 3572 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3574 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.GE, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 452:  OperatorFunction ::= TypeName . >
            //
            case 452: {
                //#line 3583 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3585 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.GT, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 453:  OperatorFunction ::= TypeName . ==
            //
            case 453: {
                //#line 3594 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3596 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.EQ, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 454:  OperatorFunction ::= TypeName . !=
            //
            case 454: {
                //#line 3605 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName TypeName = (ParsedName) getRhsSym(1);
                //#line 3607 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<Formal> formals = new ArrayList<Formal>();
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "x")));
                formals.add(nf.Formal(pos(), nf.FlagsNode(pos(), Flags.FINAL), TypeName.toType(), nf.Id(pos(), "y")));
                TypeNode tn = nf.CanonicalTypeNode(pos(), ts.Boolean());
                setResult(nf.Closure(pos(), Collections.EMPTY_LIST, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.Binary(pos(), nf.Local(pos(), nf.Id(pos(), "x")),
                                                           Binary.NE, nf.Local(pos(), nf.Id(pos(), "y")))))));
                break;
            }
     
            //
            // Rule 455:  Literal ::= IntegerLiteral$IntegerLiteral
            //
            case 455: {
                //#line 3618 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken IntegerLiteral = (IToken) getRhsIToken(1);
                //#line 3620 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.LongLiteral a = int_lit(getRhsFirstTokenIndex(1));
                setResult(nf.IntLit(pos(), IntLit.INT, a.getValue().longValue()));
                break;
            }
     
            //
            // Rule 456:  Literal ::= LongLiteral$LongLiteral
            //
            case 456: {
                //#line 3624 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken LongLiteral = (IToken) getRhsIToken(1);
                //#line 3626 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.LongLiteral a = long_lit(getRhsFirstTokenIndex(1));
                setResult(nf.IntLit(pos(), IntLit.LONG, a.getValue().longValue()));
                break;
            }
     
            //
            // Rule 457:  Literal ::= FloatingPointLiteral$FloatLiteral
            //
            case 457: {
                //#line 3630 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken FloatLiteral = (IToken) getRhsIToken(1);
                //#line 3632 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.FloatLiteral a = float_lit(getRhsFirstTokenIndex(1));
                setResult(nf.FloatLit(pos(), FloatLit.FLOAT, a.getValue().floatValue()));
                break;
            }
     
            //
            // Rule 458:  Literal ::= DoubleLiteral$DoubleLiteral
            //
            case 458: {
                //#line 3636 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken DoubleLiteral = (IToken) getRhsIToken(1);
                //#line 3638 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.DoubleLiteral a = double_lit(getRhsFirstTokenIndex(1));
                setResult(nf.FloatLit(pos(), FloatLit.DOUBLE, a.getValue().doubleValue()));
                break;
            }
     
            //
            // Rule 459:  Literal ::= BooleanLiteral
            //
            case 459: {
                //#line 3642 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                polyglot.lex.BooleanLiteral BooleanLiteral = (polyglot.lex.BooleanLiteral) getRhsSym(1);
                //#line 3644 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.BooleanLit(pos(), BooleanLiteral.getValue().booleanValue()));
                break;
            }
     
            //
            // Rule 460:  Literal ::= CharacterLiteral$CharacterLiteral
            //
            case 460: {
                //#line 3647 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken CharacterLiteral = (IToken) getRhsIToken(1);
                //#line 3649 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.CharacterLiteral a = char_lit(getRhsFirstTokenIndex(1));
                setResult(nf.CharLit(pos(), a.getValue().charValue()));
                break;
            }
     
            //
            // Rule 461:  Literal ::= StringLiteral$str
            //
            case 461: {
                //#line 3653 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken str = (IToken) getRhsIToken(1);
                //#line 3655 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                polyglot.lex.StringLiteral a = string_lit(getRhsFirstTokenIndex(1));
                setResult(nf.StringLit(pos(), a.getValue()));
                break;
            }
     
            //
            // Rule 462:  Literal ::= null
            //
            case 462: {
                
                //#line 3661 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.NullLit(pos()));
                break;
            }
     
            //
            // Rule 463:  BooleanLiteral ::= true$trueLiteral
            //
            case 463: {
                //#line 3665 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken trueLiteral = (IToken) getRhsIToken(1);
                //#line 3667 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(boolean_lit(getRhsFirstTokenIndex(1)));
                break;
            }
     
            //
            // Rule 464:  BooleanLiteral ::= false$falseLiteral
            //
            case 464: {
                //#line 3670 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken falseLiteral = (IToken) getRhsIToken(1);
                //#line 3672 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(boolean_lit(getRhsFirstTokenIndex(1)));
                break;
            }
     
            //
            // Rule 465:  ArgumentList ::= Expression
            //
            case 465: {
                //#line 3679 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(1);
                //#line 3681 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List l = new TypedList(new LinkedList(), Expr.class, false);
                l.add(Expression);
                setResult(l);
                break;
            }
     
            //
            // Rule 466:  ArgumentList ::= ArgumentList , Expression
            //
            case 466: {
                //#line 3686 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentList = (List) getRhsSym(1);
                //#line 3686 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 3688 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                ArgumentList.add(Expression);
                break;
            }
     
            //
            // Rule 467:  FieldAccess ::= Primary . Identifier
            //
            case 467: {
                //#line 3692 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 3692 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3694 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(), Primary, Identifier));
                break;
            }
     
            //
            // Rule 468:  FieldAccess ::= super . Identifier
            //
            case 468: {
                //#line 3697 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3699 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getLeftSpan())), Identifier));
                break;
            }
     
            //
            // Rule 469:  FieldAccess ::= ClassName . super$sup . Identifier
            //
            case 469: {
                //#line 3702 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 3702 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 3702 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(5);
                //#line 3704 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getRhsFirstTokenIndex(3)), ClassName.toType()), Identifier));
                break;
            }
     
            //
            // Rule 470:  FieldAccess ::= Primary . class$c
            //
            case 470: {
                //#line 3707 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 3707 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken c = (IToken) getRhsIToken(3);
                //#line 3709 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(), Primary, nf.Id(pos(getRhsFirstTokenIndex(3)), "class")));
                break;
            }
     
            //
            // Rule 471:  FieldAccess ::= super . class$c
            //
            case 471: {
                //#line 3712 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken c = (IToken) getRhsIToken(3);
                //#line 3714 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getLeftSpan())), nf.Id(pos(getRhsFirstTokenIndex(3)), "class")));
                break;
            }
     
            //
            // Rule 472:  FieldAccess ::= ClassName . super$sup . class$c
            //
            case 472: {
                //#line 3717 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 3717 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 3717 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken c = (IToken) getRhsIToken(5);
                //#line 3719 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Field(pos(getRightSpan()), nf.Super(pos(getRhsFirstTokenIndex(3)), ClassName.toType()), nf.Id(pos(getRhsFirstTokenIndex(5)), "class")));
                break;
            }
     
            //
            // Rule 473:  MethodInvocation ::= MethodName TypeArgumentsopt ( ArgumentListopt )
            //
            case 473: {
                //#line 3723 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName MethodName = (ParsedName) getRhsSym(1);
                //#line 3723 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 3723 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(4);
                //#line 3725 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Call(pos(), MethodName.prefix == null
                                                             ? null
                                                             : MethodName.prefix.toReceiver(), MethodName.name, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 474:  MethodInvocation ::= Primary . Identifier TypeArgumentsopt ( ArgumentListopt )
            //
            case 474: {
                //#line 3730 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 3730 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3730 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(4);
                //#line 3730 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(6);
                //#line 3732 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Call(pos(), Primary, Identifier, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 475:  MethodInvocation ::= super . Identifier TypeArgumentsopt ( ArgumentListopt )
            //
            case 475: {
                //#line 3735 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3735 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(4);
                //#line 3735 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(6);
                //#line 3737 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Call(pos(), nf.Super(pos(getLeftSpan())), Identifier, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 476:  MethodInvocation ::= ClassName . super$sup . Identifier TypeArgumentsopt ( ArgumentListopt )
            //
            case 476: {
                //#line 3740 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 3740 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 3740 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(5);
                //#line 3740 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(6);
                //#line 3740 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(8);
                //#line 3742 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.X10Call(pos(), nf.Super(pos(getRhsFirstTokenIndex(3)), ClassName.toType()), Identifier, TypeArgumentsopt, ArgumentListopt));
                break;
            }
     
            //
            // Rule 477:  MethodInvocation ::= Primary TypeArgumentsopt ( ArgumentListopt )
            //
            case 477: {
                //#line 3745 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 3745 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeArgumentsopt = (List) getRhsSym(2);
                //#line 3745 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentListopt = (List) getRhsSym(4);
                //#line 3747 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                if (Primary instanceof Field) {
                    Field f = (Field) Primary;
                    setResult(nf.X10Call(pos(), f.target(), f.name(), TypeArgumentsopt, ArgumentListopt));
                }
                else if (Primary instanceof AmbExpr) {
                    AmbExpr f = (AmbExpr) Primary;
                    setResult(nf.X10Call(pos(), null, f.name(), TypeArgumentsopt, ArgumentListopt));
                }
                else if (Primary instanceof Here) {
                    Here f = (Here) Primary;
                    setResult(nf.X10Call(pos(), null, nf.Id(Primary.position(), Name.make("here")), TypeArgumentsopt, ArgumentListopt));
                }
                else {
                    setResult(nf.ClosureCall(pos(), Primary, TypeArgumentsopt, ArgumentListopt));
                }
                break;
            }
     
            //
            // Rule 478:  MethodSelection ::= MethodName . TypeParametersopt ( FormalParameterListopt )
            //
            case 478: {
                //#line 3765 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName MethodName = (ParsedName) getRhsSym(1);
                //#line 3765 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(3);
                //#line 3765 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(5);
                //#line 3767 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<TypeNode> typeArgs = toTypeArgs(TypeParametersopt);
                List<TypeParamNode> typeParams = toTypeParams(TypeParametersopt);
                List<Formal> formals = toFormals(FormalParameterListopt);
                List<Expr> actuals = toActuals(FormalParameterListopt);
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), typeParams, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(), nf.X10Call(pos(), MethodName.prefix == null
                                                             ? null
                                                             : MethodName.prefix.toReceiver(), MethodName.name, typeArgs, actuals)))));
                break;
            }
     
            //
            // Rule 479:  MethodSelection ::= Primary . Identifier . TypeParametersopt ( FormalParameterListopt )
            //
            case 479: {
                //#line 3778 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Primary = (Expr) getRhsSym(1);
                //#line 3778 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3778 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(5);
                //#line 3778 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(7);
                //#line 3780 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<TypeNode> typeArgs = toTypeArgs(TypeParametersopt);
                List<TypeParamNode> typeParams = toTypeParams(TypeParametersopt);
                List<Formal> formals = toFormals(FormalParameterListopt);
                List<Expr> actuals = toActuals(FormalParameterListopt);
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), typeParams, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(),
                                               nf.X10Call(pos(), Primary, Identifier, typeArgs, actuals)))));
                break;
            }
     
            //
            // Rule 480:  MethodSelection ::= super . Identifier . TypeParametersopt ( FormalParameterListopt )
            //
            case 480: {
                //#line 3790 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(3);
                //#line 3790 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(5);
                //#line 3790 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(7);
                //#line 3792 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<TypeNode> typeArgs = toTypeArgs(TypeParametersopt);
                List<TypeParamNode> typeParams = toTypeParams(TypeParametersopt);
                List<Formal> formals = toFormals(FormalParameterListopt);
                List<Expr> actuals = toActuals(FormalParameterListopt);
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), typeParams, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(),
                                               nf.X10Call(pos(), nf.Super(pos(getLeftSpan())), Identifier, typeArgs, actuals)))));
                break;
            }
     
            //
            // Rule 481:  MethodSelection ::= ClassName . super$sup . Identifier . TypeParametersopt ( FormalParameterListopt )
            //
            case 481: {
                //#line 3802 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ClassName = (ParsedName) getRhsSym(1);
                //#line 3802 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                IToken sup = (IToken) getRhsIToken(3);
                //#line 3802 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(5);
                //#line 3802 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List TypeParametersopt = (List) getRhsSym(7);
                //#line 3802 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List FormalParameterListopt = (List) getRhsSym(9);
                //#line 3804 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                List<TypeNode> typeArgs = toTypeArgs(TypeParametersopt);
                List<TypeParamNode> typeParams = toTypeParams(TypeParametersopt);
                List<Formal> formals = toFormals(FormalParameterListopt);
                List<Expr> actuals = toActuals(FormalParameterListopt);
                TypeNode tn = nf.UnknownTypeNode(pos());
                setResult(nf.Closure(pos(), typeParams, formals, (DepParameterExpr) null, tn, Collections.EMPTY_LIST, nf.Block(pos(),
                                     nf.Return(pos(),
                                               nf.X10Call(pos(), nf.Super(pos(getRhsFirstTokenIndex(3)), ClassName.toType()), Identifier, typeArgs, actuals)))));
                break;
            }
     
            //
            // Rule 484:  PostfixExpression ::= ExpressionName
            //
            case 484: {
                //#line 3817 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ExpressionName = (ParsedName) getRhsSym(1);
                //#line 3819 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ExpressionName.toExpr());
                break;
            }
     
            //
            // Rule 487:  PostIncrementExpression ::= PostfixExpression ++
            //
            case 487: {
                //#line 3825 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PostfixExpression = (Expr) getRhsSym(1);
                //#line 3827 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), PostfixExpression, Unary.POST_INC));
                break;
            }
     
            //
            // Rule 488:  PostDecrementExpression ::= PostfixExpression --
            //
            case 488: {
                //#line 3831 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr PostfixExpression = (Expr) getRhsSym(1);
                //#line 3833 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), PostfixExpression, Unary.POST_DEC));
                break;
            }
     
            //
            // Rule 491:  UnaryExpression ::= + UnaryExpressionNotPlusMinus
            //
            case 491: {
                //#line 3839 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpressionNotPlusMinus = (Expr) getRhsSym(2);
                //#line 3841 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.POS, UnaryExpressionNotPlusMinus));
                break;
            }
     
            //
            // Rule 492:  UnaryExpression ::= - UnaryExpressionNotPlusMinus
            //
            case 492: {
                //#line 3844 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpressionNotPlusMinus = (Expr) getRhsSym(2);
                //#line 3846 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.NEG, UnaryExpressionNotPlusMinus));
                break;
            }
     
            //
            // Rule 494:  PreIncrementExpression ::= ++ UnaryExpressionNotPlusMinus
            //
            case 494: {
                //#line 3851 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpressionNotPlusMinus = (Expr) getRhsSym(2);
                //#line 3853 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.PRE_INC, UnaryExpressionNotPlusMinus));
                break;
            }
     
            //
            // Rule 495:  PreDecrementExpression ::= -- UnaryExpressionNotPlusMinus
            //
            case 495: {
                //#line 3857 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpressionNotPlusMinus = (Expr) getRhsSym(2);
                //#line 3859 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.PRE_DEC, UnaryExpressionNotPlusMinus));
                break;
            }
     
            //
            // Rule 497:  UnaryExpressionNotPlusMinus ::= ~ UnaryExpression
            //
            case 497: {
                //#line 3864 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(2);
                //#line 3866 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.BIT_NOT, UnaryExpression));
                break;
            }
     
            //
            // Rule 498:  UnaryExpressionNotPlusMinus ::= Annotations UnaryExpression
            //
            case 498: {
                //#line 3869 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List Annotations = (List) getRhsSym(1);
                //#line 3869 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(2);
                //#line 3871 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Expr e = UnaryExpression;
                e = (Expr) ((X10Ext) e.ext()).annotations(Annotations);
                setResult(e);
                break;
            }
     
            //
            // Rule 499:  UnaryExpressionNotPlusMinus ::= ! UnaryExpression
            //
            case 499: {
                //#line 3876 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(2);
                //#line 3878 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Unary(pos(), Unary.NOT, UnaryExpression));
                break;
            }
     
            //
            // Rule 501:  MultiplicativeExpression ::= MultiplicativeExpression * UnaryExpression
            //
            case 501: {
                //#line 3883 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr MultiplicativeExpression = (Expr) getRhsSym(1);
                //#line 3883 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(3);
                //#line 3885 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), MultiplicativeExpression, Binary.MUL, UnaryExpression));
                break;
            }
     
            //
            // Rule 502:  MultiplicativeExpression ::= MultiplicativeExpression / UnaryExpression
            //
            case 502: {
                //#line 3888 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr MultiplicativeExpression = (Expr) getRhsSym(1);
                //#line 3888 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(3);
                //#line 3890 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), MultiplicativeExpression, Binary.DIV, UnaryExpression));
                break;
            }
     
            //
            // Rule 503:  MultiplicativeExpression ::= MultiplicativeExpression % UnaryExpression
            //
            case 503: {
                //#line 3893 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr MultiplicativeExpression = (Expr) getRhsSym(1);
                //#line 3893 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr UnaryExpression = (Expr) getRhsSym(3);
                //#line 3895 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), MultiplicativeExpression, Binary.MOD, UnaryExpression));
                break;
            }
     
            //
            // Rule 505:  AdditiveExpression ::= AdditiveExpression + MultiplicativeExpression
            //
            case 505: {
                //#line 3900 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AdditiveExpression = (Expr) getRhsSym(1);
                //#line 3900 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr MultiplicativeExpression = (Expr) getRhsSym(3);
                //#line 3902 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), AdditiveExpression, Binary.ADD, MultiplicativeExpression));
                break;
            }
     
            //
            // Rule 506:  AdditiveExpression ::= AdditiveExpression - MultiplicativeExpression
            //
            case 506: {
                //#line 3905 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AdditiveExpression = (Expr) getRhsSym(1);
                //#line 3905 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr MultiplicativeExpression = (Expr) getRhsSym(3);
                //#line 3907 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), AdditiveExpression, Binary.SUB, MultiplicativeExpression));
                break;
            }
     
            //
            // Rule 508:  ShiftExpression ::= ShiftExpression << AdditiveExpression
            //
            case 508: {
                //#line 3912 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ShiftExpression = (Expr) getRhsSym(1);
                //#line 3912 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AdditiveExpression = (Expr) getRhsSym(3);
                //#line 3914 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ShiftExpression, Binary.SHL, AdditiveExpression));
                break;
            }
     
            //
            // Rule 509:  ShiftExpression ::= ShiftExpression >> AdditiveExpression
            //
            case 509: {
                //#line 3917 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ShiftExpression = (Expr) getRhsSym(1);
                //#line 3917 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AdditiveExpression = (Expr) getRhsSym(3);
                //#line 3919 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ShiftExpression, Binary.SHR, AdditiveExpression));
                break;
            }
     
            //
            // Rule 510:  ShiftExpression ::= ShiftExpression >>> AdditiveExpression
            //
            case 510: {
                //#line 3922 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ShiftExpression = (Expr) getRhsSym(1);
                //#line 3922 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AdditiveExpression = (Expr) getRhsSym(3);
                //#line 3924 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ShiftExpression, Binary.USHR, AdditiveExpression));
                break;
            }
     
            //
            // Rule 512:  RangeExpression ::= ShiftExpression$expr1 .. ShiftExpression$expr2
            //
            case 512: {
                //#line 3929 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr1 = (Expr) getRhsSym(1);
                //#line 3929 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr expr2 = (Expr) getRhsSym(3);
                //#line 3931 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                Expr regionCall = nf.RegionMaker(pos(), expr1, expr2);
                setResult(regionCall);
                break;
            }
     
            //
            // Rule 515:  RelationalExpression ::= RelationalExpression < RangeExpression
            //
            case 515: {
                //#line 3938 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3938 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RangeExpression = (Expr) getRhsSym(3);
                //#line 3940 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), RelationalExpression, Binary.LT, RangeExpression));
                break;
            }
     
            //
            // Rule 516:  RelationalExpression ::= RelationalExpression > RangeExpression
            //
            case 516: {
                //#line 3943 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3943 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RangeExpression = (Expr) getRhsSym(3);
                //#line 3945 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), RelationalExpression, Binary.GT, RangeExpression));
                break;
            }
     
            //
            // Rule 517:  RelationalExpression ::= RelationalExpression <= RangeExpression
            //
            case 517: {
                //#line 3948 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3948 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RangeExpression = (Expr) getRhsSym(3);
                //#line 3950 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), RelationalExpression, Binary.LE, RangeExpression));
                break;
            }
     
            //
            // Rule 518:  RelationalExpression ::= RelationalExpression >= RangeExpression
            //
            case 518: {
                //#line 3953 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3953 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RangeExpression = (Expr) getRhsSym(3);
                //#line 3955 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), RelationalExpression, Binary.GE, RangeExpression));
                break;
            }
     
            //
            // Rule 519:  RelationalExpression ::= RelationalExpression instanceof Type
            //
            case 519: {
                //#line 3958 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3958 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode Type = (TypeNode) getRhsSym(3);
                //#line 3960 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Instanceof(pos(), RelationalExpression, Type));
                break;
            }
     
            //
            // Rule 520:  RelationalExpression ::= RelationalExpression in ShiftExpression
            //
            case 520: {
                //#line 3963 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(1);
                //#line 3963 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ShiftExpression = (Expr) getRhsSym(3);
                //#line 3965 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Contains(pos(), RelationalExpression, ShiftExpression));
                break;
            }
     
            //
            // Rule 522:  EqualityExpression ::= EqualityExpression == RelationalExpression
            //
            case 522: {
                //#line 3970 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr EqualityExpression = (Expr) getRhsSym(1);
                //#line 3970 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(3);
                //#line 3972 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), EqualityExpression, Binary.EQ, RelationalExpression));
                break;
            }
     
            //
            // Rule 523:  EqualityExpression ::= EqualityExpression != RelationalExpression
            //
            case 523: {
                //#line 3975 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr EqualityExpression = (Expr) getRhsSym(1);
                //#line 3975 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr RelationalExpression = (Expr) getRhsSym(3);
                //#line 3977 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), EqualityExpression, Binary.NE, RelationalExpression));
                break;
            }
     
            //
            // Rule 524:  EqualityExpression ::= Type$t1 == Type$t2
            //
            case 524: {
                //#line 3980 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t1 = (TypeNode) getRhsSym(1);
                //#line 3980 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                TypeNode t2 = (TypeNode) getRhsSym(3);
                //#line 3982 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.SubtypeTest(pos(), t1, t2, true));
                break;
            }
     
            //
            // Rule 526:  AndExpression ::= AndExpression & EqualityExpression
            //
            case 526: {
                //#line 3987 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AndExpression = (Expr) getRhsSym(1);
                //#line 3987 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr EqualityExpression = (Expr) getRhsSym(3);
                //#line 3989 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), AndExpression, Binary.BIT_AND, EqualityExpression));
                break;
            }
     
            //
            // Rule 528:  ExclusiveOrExpression ::= ExclusiveOrExpression ^ AndExpression
            //
            case 528: {
                //#line 3994 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ExclusiveOrExpression = (Expr) getRhsSym(1);
                //#line 3994 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AndExpression = (Expr) getRhsSym(3);
                //#line 3996 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ExclusiveOrExpression, Binary.BIT_XOR, AndExpression));
                break;
            }
     
            //
            // Rule 530:  InclusiveOrExpression ::= InclusiveOrExpression | ExclusiveOrExpression
            //
            case 530: {
                //#line 4001 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr InclusiveOrExpression = (Expr) getRhsSym(1);
                //#line 4001 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ExclusiveOrExpression = (Expr) getRhsSym(3);
                //#line 4003 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), InclusiveOrExpression, Binary.BIT_OR, ExclusiveOrExpression));
                break;
            }
     
            //
            // Rule 532:  ConditionalAndExpression ::= ConditionalAndExpression && InclusiveOrExpression
            //
            case 532: {
                //#line 4008 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalAndExpression = (Expr) getRhsSym(1);
                //#line 4008 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr InclusiveOrExpression = (Expr) getRhsSym(3);
                //#line 4010 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ConditionalAndExpression, Binary.COND_AND, InclusiveOrExpression));
                break;
            }
     
            //
            // Rule 534:  ConditionalOrExpression ::= ConditionalOrExpression || ConditionalAndExpression
            //
            case 534: {
                //#line 4015 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalOrExpression = (Expr) getRhsSym(1);
                //#line 4015 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalAndExpression = (Expr) getRhsSym(3);
                //#line 4017 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Binary(pos(), ConditionalOrExpression, Binary.COND_OR, ConditionalAndExpression));
                break;
            }
     
            //
            // Rule 540:  ConditionalExpression ::= ConditionalOrExpression ? Expression : ConditionalExpression
            //
            case 540: {
                //#line 4027 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalOrExpression = (Expr) getRhsSym(1);
                //#line 4027 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr Expression = (Expr) getRhsSym(3);
                //#line 4027 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr ConditionalExpression = (Expr) getRhsSym(5);
                //#line 4029 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Conditional(pos(), ConditionalOrExpression, Expression, ConditionalExpression));
                break;
            }
     
            //
            // Rule 543:  Assignment ::= LeftHandSide AssignmentOperator AssignmentExpression
            //
            case 543: {
                //#line 4036 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr LeftHandSide = (Expr) getRhsSym(1);
                //#line 4036 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Assign.Operator AssignmentOperator = (Assign.Operator) getRhsSym(2);
                //#line 4036 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AssignmentExpression = (Expr) getRhsSym(3);
                //#line 4038 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.Assign(pos(), LeftHandSide, AssignmentOperator, AssignmentExpression));
                break;
            }
     
            //
            // Rule 544:  Assignment ::= ExpressionName$e1 ( ArgumentList ) AssignmentOperator AssignmentExpression
            //
            case 544: {
                //#line 4041 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName e1 = (ParsedName) getRhsSym(1);
                //#line 4041 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentList = (List) getRhsSym(3);
                //#line 4041 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Assign.Operator AssignmentOperator = (Assign.Operator) getRhsSym(5);
                //#line 4041 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AssignmentExpression = (Expr) getRhsSym(6);
                //#line 4043 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.SettableAssign(pos(), e1.toExpr(), ArgumentList, AssignmentOperator, AssignmentExpression));
                break;
            }
     
            //
            // Rule 545:  Assignment ::= Primary$e1 ( ArgumentList ) AssignmentOperator AssignmentExpression
            //
            case 545: {
                //#line 4046 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr e1 = (Expr) getRhsSym(1);
                //#line 4046 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                List ArgumentList = (List) getRhsSym(3);
                //#line 4046 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Assign.Operator AssignmentOperator = (Assign.Operator) getRhsSym(5);
                //#line 4046 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Expr AssignmentExpression = (Expr) getRhsSym(6);
                //#line 4048 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(nf.SettableAssign(pos(), e1, ArgumentList, AssignmentOperator, AssignmentExpression));
                break;
            }
     
            //
            // Rule 546:  LeftHandSide ::= ExpressionName
            //
            case 546: {
                //#line 4052 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                ParsedName ExpressionName = (ParsedName) getRhsSym(1);
                //#line 4054 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(ExpressionName.toExpr());
                break;
            }
     
            //
            // Rule 548:  AssignmentOperator ::= =
            //
            case 548: {
                
                //#line 4061 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.ASSIGN);
                break;
            }
     
            //
            // Rule 549:  AssignmentOperator ::= *=
            //
            case 549: {
                
                //#line 4066 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.MUL_ASSIGN);
                break;
            }
     
            //
            // Rule 550:  AssignmentOperator ::= /=
            //
            case 550: {
                
                //#line 4071 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.DIV_ASSIGN);
                break;
            }
     
            //
            // Rule 551:  AssignmentOperator ::= %=
            //
            case 551: {
                
                //#line 4076 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.MOD_ASSIGN);
                break;
            }
     
            //
            // Rule 552:  AssignmentOperator ::= +=
            //
            case 552: {
                
                //#line 4081 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.ADD_ASSIGN);
                break;
            }
     
            //
            // Rule 553:  AssignmentOperator ::= -=
            //
            case 553: {
                
                //#line 4086 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.SUB_ASSIGN);
                break;
            }
     
            //
            // Rule 554:  AssignmentOperator ::= <<=
            //
            case 554: {
                
                //#line 4091 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.SHL_ASSIGN);
                break;
            }
     
            //
            // Rule 555:  AssignmentOperator ::= >>=
            //
            case 555: {
                
                //#line 4096 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.SHR_ASSIGN);
                break;
            }
     
            //
            // Rule 556:  AssignmentOperator ::= >>>=
            //
            case 556: {
                
                //#line 4101 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.USHR_ASSIGN);
                break;
            }
     
            //
            // Rule 557:  AssignmentOperator ::= &=
            //
            case 557: {
                
                //#line 4106 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.BIT_AND_ASSIGN);
                break;
            }
     
            //
            // Rule 558:  AssignmentOperator ::= ^=
            //
            case 558: {
                
                //#line 4111 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.BIT_XOR_ASSIGN);
                break;
            }
     
            //
            // Rule 559:  AssignmentOperator ::= |=
            //
            case 559: {
                
                //#line 4116 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Assign.BIT_OR_ASSIGN);
                break;
            }
     
            //
            // Rule 562:  Catchesopt ::= $Empty
            //
            case 562: {
                
                //#line 4129 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Catch.class, false));
                break;
            }
     
            //
            // Rule 564:  Identifieropt ::= $Empty
            //
            case 564:
                setResult(null);
                break;
 
            //
            // Rule 565:  Identifieropt ::= Identifier
            //
            case 565: {
                //#line 4136 "/Users/nystrom/work/x10/1.7rel2/x10.compiler.p3/src/x10/parser/x10.g"
                Id Identifier = (Id) getRhsSym(1);
                //#line 4138 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Identifier);
                break;
            }
     
            //
            // Rule 566:  ForUpdateopt ::= $Empty
            //
            case 566: {
                
                //#line 4144 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), ForUpdate.class, false));
                break;
            }
     
            //
            // Rule 568:  Expressionopt ::= $Empty
            //
            case 568:
                setResult(null);
                break;
 
            //
            // Rule 570:  ForInitopt ::= $Empty
            //
            case 570: {
                
                //#line 4155 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), ForInit.class, false));
                break;
            }
     
            //
            // Rule 572:  SwitchLabelsopt ::= $Empty
            //
            case 572: {
                
                //#line 4162 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Case.class, false));
                break;
            }
     
            //
            // Rule 574:  SwitchBlockStatementGroupsopt ::= $Empty
            //
            case 574: {
                
                //#line 4169 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), SwitchElement.class, false));
                break;
            }
     
            //
            // Rule 576:  VariableModifiersopt ::= $Empty
            //
            case 576: {
                
                //#line 4176 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 578:  VariableInitializersopt ::= $Empty
            //
            case 578:
                setResult(null);
                break;
 
            //
            // Rule 580:  InterfaceMemberDeclarationsopt ::= $Empty
            //
            case 580: {
                
                //#line 4187 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), ClassMember.class, false));
                break;
            }
     
            //
            // Rule 582:  ExtendsInterfacesopt ::= $Empty
            //
            case 582: {
                
                //#line 4194 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypeNode.class, false));
                break;
            }
     
            //
            // Rule 584:  InterfaceModifiersopt ::= $Empty
            //
            case 584: {
                
                //#line 4201 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 586:  ClassBodyopt ::= $Empty
            //
            case 586:
                setResult(null);
                break;
 
            //
            // Rule 588:  Argumentsopt ::= $Empty
            //
            case 588: {
                
                //#line 4212 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Expr.class, false));
                break;
            }
     
            //
            // Rule 590:  ArgumentListopt ::= $Empty
            //
            case 590: {
                
                //#line 4219 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Expr.class, false));
                break;
            }
     
            //
            // Rule 592:  BlockStatementsopt ::= $Empty
            //
            case 592: {
                
                //#line 4226 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Stmt.class, false));
                break;
            }
     
            //
            // Rule 594:  ExplicitConstructorInvocationopt ::= $Empty
            //
            case 594:
                setResult(null);
                break;
 
            //
            // Rule 596:  ConstructorModifiersopt ::= $Empty
            //
            case 596: {
                
                //#line 4237 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 598:  FormalParameterListopt ::= $Empty
            //
            case 598: {
                
                //#line 4244 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Formal.class, false));
                break;
            }
     
            //
            // Rule 600:  Throwsopt ::= $Empty
            //
            case 600: {
                
                //#line 4251 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypeNode.class, false));
                break;
            }
     
            //
            // Rule 602:  MethodModifiersopt ::= $Empty
            //
            case 602: {
                
                //#line 4258 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 604:  FieldModifiersopt ::= $Empty
            //
            case 604: {
                
                //#line 4265 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(Collections.EMPTY_LIST);
                break;
            }
     
            //
            // Rule 606:  ClassBodyDeclarationsopt ::= $Empty
            //
            case 606: {
                
                //#line 4272 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), ClassMember.class, false));
                break;
            }
     
            //
            // Rule 608:  Interfacesopt ::= $Empty
            //
            case 608: {
                
                //#line 4279 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypeNode.class, false));
                break;
            }
     
            //
            // Rule 610:  Superopt ::= $Empty
            //
            case 610:
                setResult(null);
                break;
 
            //
            // Rule 612:  TypeParametersopt ::= $Empty
            //
            case 612: {
                
                //#line 4290 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypeParamNode.class, false));
                break;
            }
     
            //
            // Rule 614:  FormalParametersopt ::= $Empty
            //
            case 614: {
                
                //#line 4297 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Formal.class, false));
                break;
            }
     
            //
            // Rule 616:  Annotationsopt ::= $Empty
            //
            case 616: {
                
                //#line 4304 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), AnnotationNode.class, false));
                break;
            }
     
            //
            // Rule 618:  TypeDeclarationsopt ::= $Empty
            //
            case 618: {
                
                //#line 4311 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TopLevelDecl.class, false));
                break;
            }
     
            //
            // Rule 620:  ImportDeclarationsopt ::= $Empty
            //
            case 620: {
                
                //#line 4318 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), Import.class, false));
                break;
            }
     
            //
            // Rule 622:  PackageDeclarationopt ::= $Empty
            //
            case 622:
                setResult(null);
                break;
 
            //
            // Rule 624:  ResultTypeopt ::= $Empty
            //
            case 624:
                setResult(null);
                break;
 
            //
            // Rule 626:  TypeArgumentsopt ::= $Empty
            //
            case 626: {
                
                //#line 4333 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypeNode.class, false));
                break;
            }
     
            //
            // Rule 628:  TypePropertiesopt ::= $Empty
            //
            case 628: {
                
                //#line 4340 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), TypePropertyNode.class, false));
                break;
            }
     
            //
            // Rule 630:  Propertiesopt ::= $Empty
            //
            case 630: {
                
                //#line 4347 "/Users/nystrom/work/x10/cvs/org.eclipse.imp.lpg.metatooling/templates/btParserTemplate.gi"
                setResult(new TypedList(new LinkedList(), PropertyDecl.class, false));
                break;
            }
     
            //
            // Rule 632:  ,opt ::= $Empty
            //
            case 632:
                setResult(null);
                break;

    
            default:
                break;
        }
        return;
    }
}

