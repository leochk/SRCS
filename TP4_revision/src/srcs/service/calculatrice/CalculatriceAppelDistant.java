package srcs.service.calculatrice;

import srcs.service.SansEtat;
import srcs.service.intefaces.AppelDistant;

@SansEtat
public class CalculatriceAppelDistant implements AppelDistant, Calculatrice {
    @Override
    public Integer add(Integer a, Integer b) {
        return new Integer(a + b);
    }

    @Override
    public Integer sous(Integer a, Integer b) {
        return new Integer(a - b);
    }

    @Override
    public Integer mult(Integer a, Integer b) {
        return new Integer(a * b);
    }

    @Override
    public Calculatrice.ResDiv div(Integer a, Integer b) {
        return new Calculatrice.ResDiv(a, b);
    }
}
