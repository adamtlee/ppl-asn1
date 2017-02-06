import java.io.*;
import java.util.*;

public class VPL
{
    static final int max = 10000;
    static int[] mem = new int[max];
    static int ip, bp, sp, rv, hp, numPassed, gp, codeEnd, instructionNum;

    static String fileName;

    public static void main(String[] args) throws Exception
    {
        BufferedReader keys = new BufferedReader(
                new InputStreamReader( System.in));
        System.out.print("enter name of file containing VPL start program: ");
        fileName = keys.readLine();

        // load the program into the front part of
        // memory
        BufferedReader input = new BufferedReader( new FileReader( fileName ));
        String line;
        StringTokenizer st;
        int opcode;

        ArrayList<IntPair> labels, holes;
        labels = new ArrayList<IntPair>();
        holes = new ArrayList<IntPair>();
        int label;

        int k=0;
        do {
            line = input.readLine();
            System.out.println("parsing line [" + line + "]");
            if( line != null )
            {// extract any tokens
                st = new StringTokenizer( line );
                if( st.countTokens() > 0 )
                {// have a token, so must be an instruction (as opposed to empty line)

                    opcode = Integer.parseInt(st.nextToken());

                    // load the instruction into memory:

                    if( opcode == labelCode )
                    {// note index that comes where label would go
                        label = Integer.parseInt(st.nextToken());
                        labels.add( new IntPair( label, k ) );
                    }
                    else
                    {// opcode actually gets stored
                        mem[k] = opcode;  ++k;

                        if( opcode == callCode || opcode == jumpCode ||
                                opcode == condJumpCode )
                        {// note the hole immediately after the opcode to be filled in later
                            label = Integer.parseInt( st.nextToken() );
                            mem[k] = label;  holes.add( new IntPair( k, label ) );
                            ++k;
                        }

                        // load correct number of arguments (following label, if any):
                        for( int j=0; j<numArgs(opcode); ++j )
                        {
                            mem[k] = Integer.parseInt(st.nextToken());
                            ++k;
                        }

                    }// not a label

                }// have a token, so must be an instruction
            }// have a line
        }while( line != null );

        //System.out.println("after first scan:");
        //showMem( 0, k-1 );

        // fill in all the holes:
        int index;
        for( int m=0; m<holes.size(); ++m )
        {
            label = holes.get(m).second;
            index = -1;
            for( int n=0; n<labels.size(); ++n )
                if( labels.get(n).first == label )
                    index = labels.get(n).second;
            mem[ holes.get(m).first ] = index;
        }

        System.out.println("after replacing labels:");
        showMem( 0, k-1 );

        // initialize registers:
        bp = k;  sp = k+2;  ip = 0;  rv = -1;  hp = max;
        numPassed = 0;

        codeEnd = bp-1;

        System.out.println("Code is " );
        showMem( 0, codeEnd );

        gp = codeEnd + 1;

        // your code goes here!

        int a, b, c, n, L;
        int oldIp = 0;
        int numPast = 2;
        instructionNum = 1;
        Scanner userInput = new Scanner(System.in);

        while(ip < k){
            int op = mem[ ip ];

            a = mem[ip + 1];
            b = mem[ip + 2];
            c = mem[ip + 3];

            if( op == 0 ){
                // Do Nothing
                ip++;
            }

            else if ( op == 1) {
                //
                ip += 2;
            }
            else if (op == 2){
                mem[sp] = bp;
                mem[sp + 1] = ip + 2;
                bp = sp;
                sp += numPast;
                ip = mem[ip +1];

                numPast = 2;
            }
            else if (op == 3) {
                //
                mem[sp + numPast] = mem[bp + 2 + mem[ip +1]];
                numPast++;
                ip += 2;
            }
            else if( op == 4 ){
                // Increase SP by 1 and use it
                sp += a; // ip++?
                //
                ip += 2;
            }
            else if( op == 5){
                rv = mem[bp + 2 + a];
                ip = mem[bp + 1];
                sp = bp;
                bp = mem[bp];
            }
            else if( op == 6){
                mem[bp + 2 + a] = rv;
                ip += 2;
            }
            else if (op == 7){
                ip = a;
            }
            else if (op == 8){
                if(mem[bp + 2 + b] != 0){
                    ip = a;
                } else {
                    ip += 3;
                }
            }
            else if(op == 9){
                // a = b + c
                mem[ bp + 2 + a] = mem[bp + 2 + b] + mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 10){
                // a = b - c
                mem[ bp + 2 + a] = mem[bp + 2 + b] - mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 11){
                // a = b * c
                mem[ bp + 2 + a] = mem[bp + 2 + b] * mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 12){
                // a = b / c
                mem[ bp + 2 + a] = mem[bp + 2 + b] / mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 13){
                // a = b % c
                mem[ bp + 2 + a] = mem[bp + 2 + b] % mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 14){
                // a = b == c
                mem[ bp + 2 + a] = mem[bp + 2 + b] == mem[bp + 2 + c] ? 1 : 0;
                ip += 4;
            }
            else if(op == 15){
                // a = b != c
                if (mem[bp + 2 + b] != mem[bp + 2 + c]){
                    mem[ bp + 2 + a] = 1;
                } else {
                    mem[ bp + 2 + a] = 0;
                }
                ip += 4;
            }
            else if(op == 16){
                // a = b < c
                mem[ bp + 2 + a] = mem[bp + 2 + b] < mem[bp + 2 + c] ? 1 : 0;
                ip += 4;
            }
            else if(op == 17){
                // a = b <= c
                mem[ bp + 2 + a] = mem[bp + 2 + b] <= mem[bp + 2 + c] ? 1 : 0;
                ip += 4;
            }
            else if(op == 18){
                // a = b && c
                mem[ bp + 2 + a] = mem[bp + 2 + b] != 0 && mem[bp + 2 + c] != 0  ? 1 : 0;
                ip += 4;
            }
            else if(op == 19){
                // a = b || c
                mem[ bp + 2 + a] = mem[bp + 2 + b] != 0 || mem[bp + 2 + c] != 0 ? 1 : 0;
                ip += 4;
            }
            else if(op == 20){
                // check if cell b holds 0
                if (mem[bp + 2 + b] == 0){
                    // If true put a = 1
                    mem[bp + 2 + a] = 1;
                } else {
                    // a = 0
                    mem[bp + 2 + a] = 0;
                }
                ip += 3;
            }
            else if(op == 21){
                mem[bp + 2 + a] = mem[bp + 2 + b] * -1;
                ip += 3;
            }
            else if(op == 22){
                // Put n in cell a?
                mem[bp + 2 + a] = b;
                ip += 3;
            }
            else if(op == 23){
                // Copy value of cell b into cell a
                mem[bp + 2 + a] = mem[bp + 2 + b];
                ip += 3;
            }
            else if(op == 24){
                //
                mem[bp + 2 + a] = mem[(mem[bp + 2 + c] + mem[bp + 2 + b])];
                ip += 4;
            }
            else if(op == 25){
                mem[(mem[bp + 2 + a] + mem[bp + 2 + b])] = mem[bp + 2 + c];
                ip += 4;
            }
            else if(op == 26){
                System.exit(0);
            }
            else if(op == 27){
                // print ? take input and store in cell a
                System.out.print("? ");
                mem[bp + 2 + a] = userInput.nextInt();
                ip+= 2;
            }
            else if(op == 28){
                // print contents in cell a
                System.out.print(mem[bp + 2 + a]);
                ip += 2;

            }
            else if(op == 29){
                System.out.println();
                ip++;
            }
            else if(op == 30){
                // if value is stored between 32 and 126
                if (mem[bp + 2 + a] >= 32 && mem[bp + 2 + a] <= 126){
                    // display corresponding symbol at the console
                    System.out.print(Character.toString((char)mem[bp + 2 + a]));
                }
                ip += 2;
            }
            else if(op == 31){
                int m = mem[bp + 2 + b];
                hp -= m;
                mem[bp + 2 + a] = hp;
                ip += 3;
            }
            else if(op == 32){
                gp += mem[bp + 2 + a];
                bp += mem[bp + 2 + a]; // Added this to make similar to daniel code when he was helping me
                sp += mem[bp + 2 + a];
                ip += 2;
            }
            else if(op == 33){
                mem[gp + a] = mem[bp + 2 + b];
                ip += 3;
            }
            else if(op == 34) {
                mem[bp + 2 + a] = mem[gp + b];
                ip += 3;
            }
            if (oldIp != ip) {
                oldIp = ip;
                instructionNum++;
            }
            printMem();

//      mem[ bp + 2 + a ] == access local variable "a"

        }// end of the fetch-execute loop


    }// main

    // use symbolic names for all opcodes:

    // op to produce comment on a line by itself
    private static final int noopCode = 0;

    // ops involved with registers
    private static final int labelCode = 1;
    private static final int callCode = 2;
    private static final int passCode = 3;
    private static final int allocCode = 4;
    private static final int returnCode = 5;  // return a means "return and put
    // copy of value stored in cell a in register rv
    private static final int getRetvalCode = 6;//op a means "copy rv into cell a"
    private static final int jumpCode = 7;
    private static final int condJumpCode = 8;

    // arithmetic ops
    private static final int addCode = 9;
    private static final int subCode = 10;
    private static final int multCode = 11;
    private static final int divCode = 12;
    private static final int remCode = 13;
    private static final int equalCode = 14;
    private static final int notEqualCode = 15;
    private static final int lessCode = 16;
    private static final int lessEqualCode = 17;
    private static final int andCode = 18;
    private static final int orCode = 19;
    private static final int notCode = 20;
    private static final int oppCode = 21;

    // ops involving transfer of data
    private static final int litCode = 22;  // litCode a b means "cell a gets b"
    private static final int copyCode = 23;// copy a b means "cell a gets cell b"
    private static final int getCode = 24; // op a b means "cell a gets
    // contents of cell whose
    // index is stored in b"
    private static final int putCode = 25;  // op a b means "put contents
    // of cell b in cell whose offset is stored in cell a"

    // system-level ops:
    private static final int haltCode = 26;
    private static final int inputCode = 27;
    private static final int outputCode = 28;
    private static final int newlineCode = 29;
    private static final int symbolCode = 30;
    private static final int newCode = 31;

    // global variable ops:
    private static final int allocGlobalCode = 32;
    private static final int toGlobalCode = 33;
    private static final int fromGlobalCode = 34;

    // debug ops:
    private static final int debugCode = 35;

    // return the number of arguments after the opcode,
    // except ops that have a label return number of arguments
    // after the label, which always comes immediately after
    // the opcode
    private static int numArgs( int opcode )
    {
        // highlight specially behaving operations
        if( opcode == labelCode ) return 1;  // not used
        else if( opcode == jumpCode ) return 0;  // jump label
        else if( opcode == condJumpCode ) return 1;  // condJump label expr
        else if( opcode == callCode ) return 0;  // call label

            // for all other ops, lump by count:

        else if( opcode==noopCode ||
                opcode==haltCode ||
                opcode==newlineCode ||
                opcode==debugCode
                )
            return 0;  // op

        else if( opcode==passCode || opcode==allocCode ||
                opcode==returnCode || opcode==getRetvalCode ||
                opcode==inputCode ||
                opcode==outputCode || opcode==symbolCode ||
                opcode==allocGlobalCode
                )
            return 1;  // op arg1

        else if( opcode==notCode || opcode==oppCode ||
                opcode==litCode || opcode==copyCode || opcode==newCode ||
                opcode==toGlobalCode || opcode==fromGlobalCode

                )
            return 2;  // op arg1 arg2

        else if( opcode==addCode ||  opcode==subCode || opcode==multCode ||
                opcode==divCode ||  opcode==remCode || opcode==equalCode ||
                opcode==notEqualCode ||  opcode==lessCode ||
                opcode==lessEqualCode || opcode==andCode ||
                opcode==orCode || opcode==getCode || opcode==putCode
                )
            return 3;

        else
        {
            System.out.println("Fatal error: unknown opcode [" + opcode + "]" );
            System.exit(1);
            return -1;
        }

    }// numArgs

    private static void showMem( int a, int b )
    {
        for( int k=a; k<=b; ++k )
        {
            System.out.println( k + ": " + mem[k] );
        }
    }// showMem

    private static void printMem() {
        System.out.println();
        System.out.print("Current opcode: " + mem[ip] + " instruction #: " + instructionNum + " ip: " + ip + " rv: " + rv + "hp: " + hp + "[");
        for (int i = 0; i <= codeEnd; i++) {
            System.out.print(mem[i] + ", ");
        }
        System.out.print("||");
        for (int i = codeEnd + 1; i < sp; i++) {
            System.out.print(mem[i] + ", ");
        }
        System.out.print("||");
        for (int i = hp; i < max; i++) {
            System.out.print(mem[i] + ", ");
        }
        System.out.println("]");
    }

}// VPLstart