import java.math.BigInteger;

/**
 * Created by kaaveh on 5/18/18.
 */
public class SignaturePair {
    BigInteger r;
    BigInteger s;

    public SignaturePair(BigInteger r, BigInteger s){
        this.r= r;
        this.s= s;
    }
}
