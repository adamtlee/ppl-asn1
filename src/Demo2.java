import java.util.Scanner;

public class Demo2{
    // 0 main starts here
    public static void main(String[] args){
        Scanner keys = new Scanner( System.in );

        // 4 4 make local space
        System.out.print("? ");

        // 27 0 local cell 0 is n
        int n = keys.nextInt();

        // 22 4 40 allocate 40 spaces on heap 31
        int[] a = new int[40];

        // 22 1 0 cell 1 = 0
        int zero = 0;

        // 22 2 1 cell 2 = 1
        int one = 1;

        // 25 5 1 2
        a[zero] = one;

        // 25 5 2 2
        a[one] = one;

        // 3 0 pass n over
        // 3 5 pass a over
        // 2 1001 call f
        // 6 3
        int m = f( n, a );

        // 28 3
        System.out.println( m );
    }

    private static int f( int n, int[] a ){

        // 4 9 allocate r s k t zero (9) one two a[n]
        // 0
        int r, s, k, t;

        // 0
        int one, two;

        // 22 9 0 make literals 0, 1, 2
        // 22 6 1
        // 22 7 2
        // 24 8 1 0 temp=a[n]
        if( a[n] > 0 ){
            // 8 2001 10 if cell 10 ( a[n] <= 0 ) is 1, jump to 2001
            t = a[n];
        }
        else{
            // 0
            one = 1;

            // 0
            two = 2;

            // 10 4 0 7
            k=n-two;

            //  3 4
            //  3 1
            //  2 1001
            // 6 2
            r = f( k, a );

            // 9 4 4 6
            k = k+one;

            // 3 4
            // 3 1
            // 2 1001
            // 6 3
            s = f( k, a );

            // 9 5 2 3
            t = r + s;

            // 25 1 0 5
            a[n] = t;
        }

        return t;

    }

}
