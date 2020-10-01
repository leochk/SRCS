import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

	private static final int	MAX_NUMERO	= 5;
	private static final int	MAX_ETOILE	= 2;
	private static final int	TRY			= 100;

	public static void main(String[] args) throws InterruptedException {

		List<Integer>	tries	= new ArrayList<>();
		List<Job>	threads	= new ArrayList<>();

		Long			start	= System.currentTimeMillis();
		Long			end;
		Integer			min, max, moy;

		for (int i = 0; i < TRY; i++) {
			Job t = new Job(i); 
			t.start();
			threads.add(t);
		}
		for (int i = 0; i < TRY; i++) {
			threads.get(i).join();
			tries.add(threads.get(i).res);
		}
		
		end = System.currentTimeMillis();
		tries = tries.stream().sorted().collect(Collectors.toList());
		min = tries.get(0);
		max = tries.get(tries.size() - 1);
		moy = 0;

		for (Integer x : tries) {
			moy += x;
		}
		moy /= tries.size();

		System.out.println("Done in " + (end - start) + " ms");
		System.out.println("Min : " + min);
		System.out.println("Max : " + max);
		System.out.println("Moy : " + moy);
		System.out.println("Med : " + tries.get(tries.size()/2));

	}
	
	static class Job extends Thread {
		public Integer res;
		int id;
		public Job(int id) {
			this.id = id;
		}
		@Override
		public void run() {
			System.out.println(id + " begin simulation");

			res = Main.simulation();
			System.out.println(id + " finished");
		}
		
	}
	public static int simulation() {

		List<Integer>	numeros		= new ArrayList<>();
		List<Integer>	etoiles		= new ArrayList<>();
		List<Integer>	mesnumeros	= new ArrayList<>();
		List<Integer>	mesetoiles	= new ArrayList<>();
		Random			r			= new Random();
		int				cpt			= 0;
		boolean			b;

		tirage(numeros, etoiles, mesnumeros, mesetoiles, r);
		b = check(numeros, etoiles, mesnumeros, mesetoiles);

		while (!b) {
			numeros.clear();
			mesnumeros.clear();
			etoiles.clear();
			mesetoiles.clear();
			tirage(numeros, etoiles, mesnumeros, mesetoiles, r);
			b = check(numeros, etoiles, mesnumeros, mesetoiles);
			cpt++;
		
		}

		return cpt;
	}

	public static boolean check(List<Integer> numeros, List<Integer> etoiles,
			List<Integer> mesnumeros, List<Integer> mesetoiles) {

		for (Integer x : numeros)
			if (!mesnumeros.contains(x))
				return false;
		for (Integer x : etoiles)
			if (!mesetoiles.contains(x))
				return false;
		return true;
	}

	public static void tirage(List<Integer> numeros, List<Integer> etoiles,
			List<Integer> mesnumeros, List<Integer> mesetoiles, Random r) {

		Integer	n;
		int		i;

		for (i = 0; i < MAX_NUMERO; i++) {
			n = r.nextInt(50);
			if (numeros.contains(n))
				i--;
			else
				numeros.add(n);
		}

		for (i = 0; i < MAX_NUMERO; i++) {
			n = r.nextInt(50);
			if (mesnumeros.contains(n))
				i--;
			else
				mesnumeros.add(n);
		}

		for (i = 0; i < MAX_ETOILE; i++) {
			n = r.nextInt(12);
			if (etoiles.contains(n))
				i--;
			else
				etoiles.add(n);
		}

		for (i = 0; i < MAX_ETOILE; i++) {
			n = r.nextInt(12);
			if (mesetoiles.contains(n))
				i--;
			else
				mesetoiles.add(n);
		}

	}
}