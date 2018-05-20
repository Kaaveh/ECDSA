import java.math.BigInteger;
import java.util.Random;

/**
 * Created by kaaveh on 5/17/18.
 */
public class ECCUtility {

    public static BigInteger findd(BigInteger q){
        BigInteger min = new BigInteger("1");// lower limit
        BigInteger bigInteger1 = q.subtract(min);
        Random rnd = new Random();
        int maxNumBitLength = q.bitLength();

        BigInteger aRandomBigInt;

        aRandomBigInt = new BigInteger(maxNumBitLength, rnd);
        if (aRandomBigInt.compareTo(min) < 0)
            aRandomBigInt = aRandomBigInt.add(min);
        if (aRandomBigInt.compareTo(q) >= 0)
            aRandomBigInt = aRandomBigInt.mod(bigInteger1).add(min);

        return aRandomBigInt;
    }

    public static point AddTwoPoints(point p1, point p2, int a, BigInteger p){
        BigInteger x;
        BigInteger y;
        BigInteger s;

        if (p1.x.equals(p.add(BigInteger.ONE)) && p1.y.equals(p.add(BigInteger.ONE))){
            if (p1.x.equals(p2.x) && p2.y.equals(p2.y)){
                x= p.add(BigInteger.ONE);
                y= p.add(BigInteger.ONE);
            }else {
                x= p2.x;
                y= p2.y;
            }
        }else if (p2.x.equals(p.add(BigInteger.ONE)) && p2.y.equals(p.add(BigInteger.ONE))){
            x= p1.x;
            y= p1.y;
        }else if (p1.x.equals(p2.x) && p1.y.equals(p2.y)){
            if (p1.y.multiply(BigInteger.valueOf(2)).gcd(p).equals(BigInteger.ONE)){
                s = (p1.x.pow(2).multiply(BigInteger.valueOf(3)).add(BigInteger.valueOf(a)))
                        .multiply(p1.y.multiply(BigInteger.valueOf(2)).modInverse(p)).mod(p); // (3x1^2+a)/2y1 (mod p)
                x = s.pow(2).subtract(p1.x).subtract(p2.x).mod(p);  //s^2 - x1 - x2 (mod p)
                y = s.multiply(p1.x.subtract(x)).subtract(p1.y).mod(p);  //s(x1-x3) - y1 (mod p)
            }else {
                x= p.add(BigInteger.ONE);
                y= p.add(BigInteger.ONE);
            }
        }else {
            if (p2.x.subtract(p1.x).add(p).mod(p).gcd(p).equals(BigInteger.ONE)){
                s = (p2.y.subtract(p1.y))
                        .multiply(p2.x.subtract(p1.x).modInverse(p)).mod(p); // (y2-y1)/(x2-x1) (mod p)
                x = s.pow(2).subtract(p1.x).subtract(p2.x).mod(p);  //s^2 - x1 - x2 (mod p)
                y = s.multiply(p1.x.subtract(x)).subtract(p1.y).mod(p);  //s(x1-x3) - y1 (mod p)
            }else {
                x= p.add(BigInteger.ONE);
                y= p.add(BigInteger.ONE);
            }
        }

        point r = new point(x,y);
        return r;
    }

    public static point DoubleAndAdd(BigInteger d, point p1, int a, BigInteger p){
        BigInteger x = p.add(BigInteger.ONE);
        BigInteger y = p.add(BigInteger.ONE);
        point r = new point(x,y);
        String dBinary = d.toString(2);

        for (int i=0; i<dBinary.length(); i++){
            r = AddTwoPoints(r, r, a, p);
            if (dBinary.charAt(i) == '1'){
                r = AddTwoPoints(r, p1, a, p);
            }
        }
        return r;
    }

    public static SignaturePair ECDSASignatureGeneration(point p1, int a, BigInteger p, BigInteger q, BigInteger d){
        BigInteger r;
        BigInteger s;
        BigInteger Ke;

        while (true) {
            Ke = new BigInteger(q.bitLength(), new Random());
            if (Ke.gcd(p).equals(BigInteger.ONE))
                break;
        }

        point RPoint = DoubleAndAdd(Ke, p1, a, p);

        r = RPoint.x;
        s= r.multiply(d).add(BigInteger.valueOf(Main.message.hashCode()))
                .multiply(Ke.modInverse(q)).mod(q);

        return new SignaturePair(r, s);
    }

    public static String ECDSASignatureVerification(BigInteger r, BigInteger s, int a, BigInteger p, BigInteger q, point basePoint, point B){
        BigInteger w = s.modInverse(q);
        BigInteger u1 = w.multiply(BigInteger.valueOf(Main.message.hashCode())).mod(q);
        BigInteger u2 = w.multiply(r).mod(q);

        point p1 = DoubleAndAdd(u1, basePoint, a, p);
        point p2 = DoubleAndAdd(u2, B, a, p);
        point p3 = AddTwoPoints(p1, p2, a, p);

        if (p3.x.equals(r.mod(q))){
            return "Validation Succeed!";
        }else {
            return "Validation Failed!";
        }
    }
}
