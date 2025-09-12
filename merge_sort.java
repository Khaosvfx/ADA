import java.util.*;

public class SistemaInventario {
    
    private static long comparaciones = 0;
    
    static class Producto {
        int id;
        String nombre;
        double precio;
        int stock;
        String categoria;
        
        public Producto(int id, String nombre, double precio, int stock, String categoria) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
            this.categoria = categoria;
        }
        
        @Override
        public String toString() {
            return String.format("ID:%d %s $%.2f Stock:%d [%s]", 
                               id, nombre, precio, stock, categoria);
        }
    }
    
    public static class MergeSort {
        
        public static void ordenarPorPrecio(Producto[] productos) {
            comparaciones = 0;
            mergeSortPrecio(productos, 0, productos.length - 1);
        }
        
        public static void ordenarPorStock(Producto[] productos) {
            comparaciones = 0;
            mergeSortStock(productos, 0, productos.length - 1);
        }
        
        private static void mergeSortPrecio(Producto[] arr, int izq, int der) {
            if (izq < der) {
                int medio = izq + (der - izq) / 2;
                mergeSortPrecio(arr, izq, medio);
                mergeSortPrecio(arr, medio + 1, der);
                mergePrecio(arr, izq, medio, der);
            }
        }
        
        private static void mergePrecio(Producto[] arr, int izq, int medio, int der) {
            int n1 = medio - izq + 1;
            int n2 = der - medio;
            
            Producto[] L = new Producto[n1];
            Producto[] R = new Producto[n2];
            
            System.arraycopy(arr, izq, L, 0, n1);
            System.arraycopy(arr, medio + 1, R, 0, n2);
            
            int i = 0, j = 0, k = izq;
            
            while (i < n1 && j < n2) {
                comparaciones++;
                if (L[i].precio <= R[j].precio) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            
            while (i < n1) arr[k++] = L[i++];
            while (j < n2) arr[k++] = R[j++];
        }
        
        private static void mergeSortStock(Producto[] arr, int izq, int der) {
            if (izq < der) {
                int medio = izq + (der - izq) / 2;
                mergeSortStock(arr, izq, medio);
                mergeSortStock(arr, medio + 1, der);
                mergeStock(arr, izq, medio, der);
            }
        }
        
        private static void mergeStock(Producto[] arr, int izq, int medio, int der) {
            int n1 = medio - izq + 1;
            int n2 = der - medio;
            
            Producto[] L = new Producto[n1];
            Producto[] R = new Producto[n2];
            
            System.arraycopy(arr, izq, L, 0, n1);
            System.arraycopy(arr, medio + 1, R, 0, n2);
            
            int i = 0, j = 0, k = izq;
            
            while (i < n1 && j < n2) {
                comparaciones++;
                if (L[i].stock >= R[j].stock) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            
            while (i < n1) arr[k++] = L[i++];
            while (j < n2) arr[k++] = R[j++];
        }
    }
    
    public static class GestorInventario {
        private List<Producto> inventario;
        
        public GestorInventario() {
            inventario = new ArrayList<>();
        }
        
        public void agregar(Producto producto) {
            inventario.add(producto);
        }
        
        public Producto[] obtenerArray() {
            return inventario.toArray(new Producto[0]);
        }
        
        public void mostrar(String titulo, Producto[] productos, int limite) {
            System.out.println("=== " + titulo + " ===");
            for (int i = 0; i < Math.min(productos.length, limite); i++) {
                System.out.println((i + 1) + ". " + productos[i]);
            }
            System.out.println("Comparaciones realizadas: " + comparaciones);
            System.out.println();
        }
        
        public Producto[] obtenerBajoStock(int umbral) {
            Producto[] productos = obtenerArray();
            MergeSort.ordenarPorStock(productos);
            
            List<Producto> resultado = new ArrayList<>();
            for (Producto p : productos) {
                if (p.stock < umbral) {
                    resultado.add(p);
                }
            }
            return resultado.toArray(new Producto[0]);
        }
        
        public Producto[] obtenerMasCaros(int cantidad) {
            Producto[] productos = obtenerArray();
            MergeSort.ordenarPorPrecio(productos);
            
            int inicio = Math.max(0, productos.length - cantidad);
            Producto[] resultado = new Producto[productos.length - inicio];
            
            for (int i = inicio, j = 0; i < productos.length; i++, j++) {
                resultado[j] = productos[productos.length - 1 - j];
            }
            
            return Arrays.copyOf(resultado, Math.min(cantidad, resultado.length));
        }
        
        public double calcularValorTotal() {
            double total = 0;
            for (Producto p : inventario) {
                total += p.precio * p.stock;
            }
            return total;
        }
        
        public void generarReporte() {
            System.out.println("=== REPORTE DE INVENTARIO ===");
            
            Producto[] bajoStock = obtenerBajoStock(10);
            System.out.println("PRODUCTOS CON BAJO STOCK (< 10 unidades): " + bajoStock.length);
            for (int i = 0; i < Math.min(5, bajoStock.length); i++) {
                System.out.println("⚠️  " + bajoStock[i]);
            }
            System.out.println();
            
            Producto[] masCaros = obtenerMasCaros(5);
            System.out.println("TOP 5 PRODUCTOS MAS CAROS:");
            for (int i = 0; i < masCaros.length; i++) {
                System.out.println("💰 " + masCaros[i]);
            }
            System.out.println();
            
            System.out.println("VALOR TOTAL INVENTARIO: $" + String.format("%.2f", calcularValorTotal()));
            System.out.println("TOTAL PRODUCTOS: " + inventario.size());
            System.out.println();
        }
    }
    
    public static class GeneradorDatos {
        private static Random random = new Random();
        private static String[] nombres = {
            "Laptop Gaming", "Smartphone Pro", "Monitor 4K", "Teclado Mecanico", 
            "Mouse Gaming", "Audifonos Bluetooth", "Tablet Pro", "Camara Digital",
            "Impresora Laser", "Disco SSD", "Memoria RAM", "Tarjeta Grafica"
        };
        
        private static String[] categorias = {
            "Computadoras", "Electrónicos", "Gaming", "Audio", "Oficina"
        };
        
        public static Producto[] generar(int cantidad) {
            Producto[] productos = new Producto[cantidad];
            
            for (int i = 0; i < cantidad; i++) {
                int id = 1000 + i;
                String nombre = nombres[random.nextInt(nombres.length)] + " " + (i + 1);
                double precio = 50 + random.nextDouble() * 1500;
                int stock = random.nextInt(50);
                String categoria = categorias[random.nextInt(categorias.length)];
                
                productos[i] = new Producto(id, nombre, precio, stock, categoria);
            }
            
            return productos;
        }
        
        public static Producto[] generarEjemplo() {
            return new Producto[] {
                new Producto(1001, "Laptop ASUS Gaming", 1299.99, 5, "Computadoras"),
                new Producto(1002, "iPhone 15 Pro", 999.99, 12, "Electrónicos"),
                new Producto(1003, "Monitor LG 4K", 349.99, 8, "Computadoras"),
                new Producto(1004, "Teclado Razer", 129.99, 25, "Gaming"),
                new Producto(1005, "Mouse Logitech", 79.99, 3, "Gaming"),
                new Producto(1006, "SSD Samsung 1TB", 89.99, 30, "Computadoras"),
                new Producto(1007, "AirPods Pro", 249.99, 7, "Audio"),
                new Producto(1008, "iPad Pro", 1099.99, 2, "Electrónicos"),
                new Producto(1009, "Impresora HP", 199.99, 15, "Oficina"),
                new Producto(1010, "Cámara Canon", 799.99, 1, "Electrónicos")
            };
        }
    }
    
    public static void analizarRendimiento() {
        System.out.println("=== ANALISIS DE RENDIMIENTO ===");
        int[] tamanos = {100, 500, 1000, 5000, 10000};
        
        System.out.printf("%-10s | %-15s | %-12s\n", "Productos", "Tiempo (ms)", "Comparaciones");
        System.out.println("-".repeat(42));
        
        for (int tamano : tamanos) {
            Producto[] productos = GeneradorDatos.generar(tamano);
            
            long inicio = System.nanoTime();
            MergeSort.ordenarPorPrecio(productos);
            long tiempo = System.nanoTime() - inicio;
            
            System.out.printf("%-10d | %-15.3f | %-12d\n", 
                            tamano, tiempo/1_000_000.0, comparaciones);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("SISTEMA DE INVENTARIO CON MERGESORT");
        System.out.println("=".repeat(40));
        System.out.println();
        
        GestorInventario gestor = new GestorInventario();
        Producto[] productos = GeneradorDatos.generarEjemplo();
        
        for (Producto p : productos) {
            gestor.agregar(p);
        }
        
        System.out.println("=== CASOS DE USO ===");
        
        Producto[] porPrecio = gestor.obtenerArray();
        MergeSort.ordenarPorPrecio(porPrecio);
        gestor.mostrar("PRODUCTOS POR PRECIO (MENOR A MAYOR)", porPrecio, 10);
        
        Producto[] porStock = gestor.obtenerArray();
        MergeSort.ordenarPorStock(porStock);
        gestor.mostrar("PRODUCTOS POR STOCK (MAYOR A MENOR)", porStock, 10);
        
        gestor.generarReporte();
        
        analizarRendimiento();
        
        System.out.println("=== VENTAJAS DE MERGESORT ===");
        System.out.println("✓ Rendimiento estable O(n log n)");
        System.out.println("✓ Eficiente para inventarios grandes");
        System.out.println("✓ Algoritmo estable y confiable");
        System.out.println("✓ Multiples criterios de ordenacion");
    }
}