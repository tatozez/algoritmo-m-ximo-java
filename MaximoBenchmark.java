import java.util.Random;

public class MaximoBenchmark {

    // 1) Algoritmo del máximo (O(n))
    public static int encontrarMaximo(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o vacío");
        }

        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }

    // 2A) Arreglo aleatorio
    public static int[] generarAleatorio(int n, int bound, long seed) {
        Random r = new Random(seed);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = r.nextInt(bound); // [0, bound)
        }
        return arr;
    }

    // 2B) Arreglo ordenado creciente
    public static int[] generarCreciente(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i; // 0..n-1
        }
        return arr;
    }

    // 2C) Arreglo ordenado decreciente
    public static int[] generarDecreciente(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (n - 1) - i; // n-1..0
        }
        return arr;
    }

    // Medición: hace "warm-up" y luego promedia varias repeticiones
    public static long medirPromedioNano(int[] arr, int repeticiones, int warmup) {
        // Warm-up (para que el JIT optimice)
        for (int i = 0; i < warmup; i++) {
            encontrarMaximo(arr);
        }

        long total = 0;
        int dummy = 0; // evita que el compilador "ignore" el resultado
        for (int i = 0; i < repeticiones; i++) {
            long inicio = System.nanoTime();
            dummy ^= encontrarMaximo(arr);
            long fin = System.nanoTime();
            total += (fin - inicio);
        }

        if (dummy == 123456789) System.out.println("Improbable: " + dummy);
        return total / repeticiones;
    }

    public static void main(String[] args) {
        // 3) Tamaños de entrada (5 tamaños)
        int[] tamanios = {10, 100, 1000, 10000, 100000};

        // Ajustes de benchmark
        int repeticiones = 30;
        int warmup = 10;

        System.out.println("Benchmark: Algoritmo del máximo (System.nanoTime)");
        System.out.println("Repeticiones: " + repeticiones + " | Warm-up: " + warmup);
        System.out.println();

        for (int n : tamanios) {
            int[] aleatorio = generarAleatorio(n, 1_000_000, 42L);
            int[] creciente = generarCreciente(n);
            int[] decreciente = generarDecreciente(n);

            long tA = medirPromedioNano(aleatorio, repeticiones, warmup);
            long tB = medirPromedioNano(creciente, repeticiones, warmup);
            long tC = medirPromedioNano(decreciente, repeticiones, warmup);

            // Mostramos también el máximo para verificar
            int mA = encontrarMaximo(aleatorio);
            int mB = encontrarMaximo(creciente);
            int mC = encontrarMaximo(decreciente);

            System.out.println("Tamaño n = " + n);
            System.out.println("  Aleatorio   -> tiempo prom: " + tA + " ns | max: " + mA);
            System.out.println("  Creciente   -> tiempo prom: " + tB + " ns | max: " + mB);
            System.out.println("  Decreciente -> tiempo prom: " + tC + " ns | max: " + mC);
            System.out.println();
        }

        // Ejemplo del enunciado
        int[] ejemplo = {4, 9, 2, 10, 7};
        System.out.println("Ejemplo [4, 9, 2, 10, 7] -> max: " + encontrarMaximo(ejemplo));
    }
}

