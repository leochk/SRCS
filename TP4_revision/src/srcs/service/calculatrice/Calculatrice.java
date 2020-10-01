package srcs.service.calculatrice;

import java.io.Serializable;

public interface Calculatrice {
    static class ResDiv implements Serializable {
        Integer q;
        Integer r;
        public ResDiv (Integer a , Integer b)  {
            q = a/b;
            r = a%b;
        }
        public int getQuotient() {
            return q;
        }
        public int getReste() {
            return r;
        }
    }
    public Integer add (Integer a, Integer b);
    public Integer sous (Integer a, Integer b);
    public Integer mult (Integer a, Integer b);
    public ResDiv div (Integer a, Integer b);

}
