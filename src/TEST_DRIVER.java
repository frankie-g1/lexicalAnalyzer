public class TEST_DRIVER {

    public static void main(String[] args) {
        String eq = "8 + 2 - (5*2);.";
        lexical_remasterd test = new lexical_remasterd(eq);

        int nextToken= test.nextToken();
        String s = test.getTokenString();
        while(nextToken != 308) // period
        {
            System.out.println(s + " -> " + nextToken);
            nextToken = test.nextToken();
            s = test.getTokenString();

        }
    }
}
