import java.math.BigInteger;

/**
 * Created by kaaveh on 5/17/18.
 */
public class Main {

    public static String message = "This is sample!";

    public static void main(String[] args) {
        int a =-3;
        BigInteger b =new BigInteger("2455155546008943817740293915197451784769108058161191238065");
        point basePoint = new point(new BigInteger("602046282375688656758213480587526111916698976636884684818")
                ,new BigInteger("174050332293622031404857552280219410364023488927386650641"));
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger q = new BigInteger("6277101735386680763835789423176059013767194773182842284081");

        System.out.println("Choosing d(0<d<q):");
        BigInteger d = ECCUtility.findd(q);
        System.out.println(d);

        point bPoint = ECCUtility.DoubleAndAdd(d, basePoint, a, p);
        System.out.println("Bx: " + bPoint.x);
        System.out.println("By: " + bPoint.y);

        SignaturePair signaturePair = ECCUtility.ECDSASignatureGeneration(basePoint, a, p, q, d);
        System.out.println(ECCUtility.ECDSASignatureVerification(signaturePair.r, signaturePair.s, a, p, q, basePoint, bPoint));
    }
}
