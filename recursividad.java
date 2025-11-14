import java.util.*;

public class SistemaInventarioAvanzado {
    
    private static long comparacionesSort = 0;
    private static long operacionesHash = 0;
    private static long operacionesBacktrack = 0;
    
    static class Producto {
        int id;
        String nombre;
        double precio;
        int stock;
        String categoria;
        int peso;
        
        public Producto(int id, String nombre, double precio, int stock, String categoria, int peso) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
            this.categoria = categoria;
            this.peso = peso;
        }
        
        @Override
        public String toString() {
            return String.format("ID:%d %s $%.2f Stock:%d Peso:%dg [%s]", 
                               id, nombre, precio, stock, peso, categoria);
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }
    
    public static class MergeSort {
        
        public static void ordenar(Producto[] productos, String criterio) {
            comparacionesSort = 0;
            mergeSortRecursivo(productos, 0, productos.length - 1, criterio);
        }
        
        private static void mergeSortRecursivo(Producto[] arr, int izq, int der, String criterio) {
            if (izq < der) {
                int medio = izq + (der - izq) / 2;
                mergeSortRecursivo(arr, izq, medio, criterio);
                mergeSortRecursivo(arr, medio + 1, der, criterio);
                merge(arr, izq, medio, der, criterio);
            }
        }
        
        private static void merge(Producto[] arr, int izq, int medio, int der, String criterio) {
            int n1 = medio - izq + 1;
            int n2 = der - medio;
            
            Producto[] L = new Producto[n1];
            Producto[] R = new Producto[n2];
            
            System.arraycopy(arr, izq, L, 0, n1);
            System.arraycopy(arr, medio + 1, R, 0, n2);
            
            int i = 0, j = 0, k = izq;
            
            while (i < n1 && j < n2) {
                comparacionesSort++;
                if (comparar(L[i], R[j], criterio) <= 0) {
                    arr[k++] = L[i++];
                } else {
                    arr[k++] = R[j++];
                }
            }
            
            while (i < n1) arr[k++] = L[i++];
            while (j < n2) arr[k++] = R[j++];
        }
        
        private static int comparar(Producto p1, Producto p2, String criterio) {
            switch(criterio) {
                case "precio": return Double.compare(p1.precio, p2.precio);
                case "stock": return Integer.compare(p2.stock, p1.stock);
                case "peso": return Integer.compare(p1.peso, p2.peso);
                default: return Integer.compare(p1.id, p2.id);
            }
        }
    }
    
    public static class QuickSort {
        
        public static void ordenar(Producto[] productos, String criterio) {
            comparacionesSort = 0;
            quickSortRecursivo(productos, 0, productos.length - 1, criterio);
        }
        
        private static void quickSortRecursivo(Producto[] arr, int bajo, int alto, String criterio) {
            if (bajo < alto) {
                int pivote = particionar(arr, bajo, alto, criterio);
                quickSortRecursivo(arr, bajo, pivote - 1, criterio);
                quickSortRecursivo(arr, pivote + 1, alto, criterio);
            }
        }
        
        private static int particionar(Producto[] arr, int bajo, int alto, String criterio) {
            Producto pivote = arr[alto];
            int i = bajo - 1;
            
            for (int j = bajo; j < alto; j++) {
                comparacionesSort++;
                if (comparar(arr[j], pivote, criterio) <= 0) {
                    i++;
                    intercambiar(arr, i, j);
                }
            }
            intercambiar(arr, i + 1, alto);
            return i + 1;
        }
        
        private static void intercambiar(Producto[] arr, int i, int j) {
            Producto temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        
        private static int comparar(Producto p1, Producto p2, String criterio) {
            switch(criterio) {
                case "precio": return Double.compare(p1.precio, p2.precio);
                case "stock": return Integer.compare(p2.stock, p1.stock);
                case "peso": return Integer.compare(p1.peso, p2.peso);
                default: return Integer.compare(p1.id, p2.id);
            }
        }
    }
    
    public static class ShellSort {
        
        public static void ordenar(Producto[] productos, String criterio) {
            comparacionesSort = 0;
            int n = productos.length;
            
            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    Producto temp = productos[i];
                    int j;
                    
                    for (j = i; j >= gap && comparar(productos[j - gap], temp, criterio) > 0; j -= gap) {
                        comparacionesSort++;
                        productos[j] = productos[j - gap];
                    }
                    productos[j] = temp;
                }
            }
        }
        
        private static int comparar(Producto p1, Producto p2, String criterio) {
            switch(criterio) {
                case "precio": return Double.compare(p1.precio, p2.precio);
                case "stock": return Integer.compare(p2.stock, p1.stock);
                case "peso": return Integer.compare(p1.peso, p2.peso);
                default: return Integer.compare(p1.id, p2.id);
            }
        }
    }
    
    public static class BusquedaDivideVenceras {
        
        public static Producto busquedaBinariaRecursiva(Producto[] productos, int id, int izq, int der) {
            if (izq > der) return null;
            
            int medio = izq + (der - izq) / 2;
            comparacionesSort++;
            
            if (productos[medio].id == id) {
                return productos[medio];
            } else if (productos[medio].id > id) {
                return busquedaBinariaRecursiva(productos, id, izq, medio - 1);
            } else {
                return busquedaBinariaRecursiva(productos, id, medio + 1, der);
            }
        }
        
        public static double encontrarMaximoPrecio(Producto[] productos, int izq, int der) {
            if (izq == der) {
                return productos[izq].precio;
            }
            
            int medio = izq + (der - izq) / 2;
            double maxIzq = encontrarMaximoPrecio(productos, izq, medio);
            double maxDer = encontrarMaximoPrecio(productos, medio + 1, der);
            
            return Math.max(maxIzq, maxDer);
        }
        
        public static int contarProductosEnRango(Producto[] productos, double precioMin, double precioMax, int izq, int der) {
            if (izq > der) return 0;
            
            int medio = izq + (der - izq) / 2;
            int contador = 0;
            
            if (productos[medio].precio >= precioMin && productos[medio].precio <= precioMax) {
                contador = 1;
            }
            
            contador += contarProductosEnRango(productos, precioMin, precioMax, izq, medio - 1);
            contador += contarProductosEnRango(productos, precioMin, precioMax, medio + 1, der);
            
            return contador;
        }
    }
    
    public static class BacktrackingInventario {
        
        public static List<Producto> encontrarComboPresupuesto(Producto[] productos, double presupuesto) {
            operacionesBacktrack = 0;
            List<Producto> mejorCombo = new ArrayList<>();
            List<Producto> comboActual = new ArrayList<>();
            backtrackPresupuesto(productos, 0, presupuesto, 0, comboActual, mejorCombo);
            return mejorCombo;
        }
        
        private static void backtrackPresupuesto(Producto[] productos, int indice, double presupuesto, 
                                                 double totalActual, List<Producto> comboActual, 
                                                 List<Producto> mejorCombo) {
            operacionesBacktrack++;
            
            if (totalActual <= presupuesto) {
                if (totalActual > calcularTotal(mejorCombo) && comboActual.size() > 0) {
                    mejorCombo.clear();
                    mejorCombo.addAll(comboActual);
                }
            }
            
            if (indice >= productos.length || totalActual > presupuesto) {
                return;
            }
            
            comboActual.add(productos[indice]);
            backtrackPresupuesto(productos, indice + 1, presupuesto, 
                               totalActual + productos[indice].precio, comboActual, mejorCombo);
            comboActual.remove(comboActual.size() - 1);
            
            backtrackPresupuesto(productos, indice + 1, presupuesto, totalActual, comboActual, mejorCombo);
        }
        
        public static List<Producto> mochilaPeso(Producto[] productos, int capacidadMaxima) {
            operacionesBacktrack = 0;
            List<Producto> mejorCombinacion = new ArrayList<>();
            List<Producto> combinacionActual = new ArrayList<>();
            backtrackMochila(productos, 0, capacidadMaxima, 0, 0, combinacionActual, mejorCombinacion);
            return mejorCombinacion;
        }
        
        private static void backtrackMochila(Producto[] productos, int indice, int capacidad,
                                            int pesoActual, double valorActual,
                                            List<Producto> combinacionActual, List<Producto> mejorCombinacion) {
            operacionesBacktrack++;
            
            if (pesoActual <= capacidad) {
                if (valorActual > calcularTotal(mejorCombinacion)) {
                    mejorCombinacion.clear();
                    mejorCombinacion.addAll(combinacionActual);
                }
            }
            
            if (indice >= productos.length || pesoActual > capacidad) {
                return;
            }
            
            if (pesoActual + productos[indice].peso <= capacidad) {
                combinacionActual.add(productos[indice]);
                backtrackMochila(productos, indice + 1, capacidad,
                               pesoActual + productos[indice].peso,
                               valorActual + productos[indice].precio,
                               combinacionActual, mejorCombinacion);
                combinacionActual.remove(combinacionActual.size() - 1);
            }
            
            backtrackMochila(productos, indice + 1, capacidad, pesoActual, valorActual,
                           combinacionActual, mejorCombinacion);
        }
        
        public static List<List<Producto>> encontrarCombinacionesCategoria(Producto[] productos, 
                                                                           String[] categoriasRequeridas) {
            operacionesBacktrack = 0;
            List<List<Producto>> todasCombinaciones = new ArrayList<>();
            List<Producto> combinacionActual = new ArrayList<>();
            Set<String> categoriasActuales = new HashSet<>();
            
            backtrackCategorias(productos, 0, categoriasRequeridas, categoriasActuales,
                              combinacionActual, todasCombinaciones);
            
            return todasCombinaciones;
        }
        
        private static void backtrackCategorias(Producto[] productos, int indice,
                                               String[] categoriasRequeridas,
                                               Set<String> categoriasActuales,
                                               List<Producto> combinacionActual,
                                               List<List<Producto>> todasCombinaciones) {
            operacionesBacktrack++;
            
            if (categoriasActuales.size() == categoriasRequeridas.length) {
                todasCombinaciones.add(new ArrayList<>(combinacionActual));
                return;
            }
            
            if (indice >= productos.length) {
                return;
            }
            
            if (Arrays.asList(categoriasRequeridas).contains(productos[indice].categoria) &&
                !categoriasActuales.contains(productos[indice].categoria)) {
                
                combinacionActual.add(productos[indice]);
                categoriasActuales.add(productos[indice].categoria);
                
                backtrackCategorias(productos, indice + 1, categoriasRequeridas,
                                  categoriasActuales, combinacionActual, todasCombinaciones);
                
                combinacionActual.remove(combinacionActual.size() - 1);
                categoriasActuales.remove(productos[indice].categoria);
            }
            
            backtrackCategorias(productos, indice + 1, categoriasRequeridas,
                              categoriasActuales, combinacionActual, todasCombinaciones);
        }
        
        private static double calcularTotal(List<Producto> productos) {
            double total = 0;
            for (Producto p : productos) {
                total += p.precio;
            }
            return total;
        }
    }
    
    public static class TablaHash {
        private HashMap<Integer, Producto> tablaPorId;
        private HashMap<String, List<Producto>> tablaPorCategoria;
        
        public TablaHash() {
            tablaPorId = new HashMap<>();
            tablaPorCategoria = new HashMap<>();
        }
        
        public void insertar(Producto producto) {
            operacionesHash++;
            tablaPorId.put(producto.id, producto);
            
            operacionesHash++;
            tablaPorCategoria.computeIfAbsent(producto.categoria, k -> new ArrayList<>()).add(producto);
        }
        
        public Producto buscarPorId(int id) {
            operacionesHash++;
            return tablaPorId.get(id);
        }
        
        public List<Producto> buscarPorCategoria(String categoria) {
            operacionesHash++;
            return tablaPorCategoria.getOrDefault(categoria, new ArrayList<>());
        }
    }
    
    public static class GestorInventario {
        private List<Producto> inventario;
        private TablaHash tablaHash;
        
        public GestorInventario() {
            inventario = new ArrayList<>();
            tablaHash = new TablaHash();
        }
        
        public void agregar(Producto producto) {
            inventario.add(producto);
            tablaHash.insertar(producto);
        }
        
        public Producto[] obtenerArray() {
            return inventario.toArray(new Producto[0]);
        }
        
        public void compararAlgoritmos() {
            System.out.println("=== COMPARACION DE ALGORITMOS DE ORDENACION ===");
            
            Producto[] productos1 = obtenerArray();
            Producto[] productos2 = Arrays.copyOf(productos1, productos1.length);
            Producto[] productos3 = Arrays.copyOf(productos1, productos1.length);
            
            System.out.printf("%-15s | %-12s | %-15s\n", "Algoritmo", "Tiempo (ms)", "Comparaciones");
            System.out.println("-".repeat(50));
            
            long inicio = System.nanoTime();
            MergeSort.ordenar(productos1, "precio");
            long tiempoMerge = System.nanoTime() - inicio;
            long compMerge = comparacionesSort;
            
            inicio = System.nanoTime();
            QuickSort.ordenar(productos2, "precio");
            long tiempoQuick = System.nanoTime() - inicio;
            long compQuick = comparacionesSort;
            
            inicio = System.nanoTime();
            ShellSort.ordenar(productos3, "precio");
            long tiempoShell = System.nanoTime() - inicio;
            long compShell = comparacionesSort;
            
            System.out.printf("%-15s | %-12.3f | %-15d\n", "MergeSort", tiempoMerge/1_000_000.0, compMerge);
            System.out.printf("%-15s | %-12.3f | %-15d\n", "QuickSort", tiempoQuick/1_000_000.0, compQuick);
            System.out.printf("%-15s | %-12.3f | %-15d\n", "ShellSort", tiempoShell/1_000_000.0, compShell);
            System.out.println();
        }
        
        public void demostrarDivideVenceras() {
            System.out.println("=== DIVIDE Y VENCERAS ===");
            
            Producto[] productos = obtenerArray();
            QuickSort.ordenar(productos, "id");
            
            System.out.println("1. Búsqueda Binaria (Divide y Venceras):");
            Producto encontrado = BusquedaDivideVenceras.busquedaBinariaRecursiva(productos, 1005, 0, productos.length - 1);
            if (encontrado != null) {
                System.out.println("   Encontrado: " + encontrado);
            }
            System.out.println("   Comparaciones: " + comparacionesSort);
            System.out.println();
            
            System.out.println("2. Encontrar Maximo Precio (Divide y Venceras):");
            double maxPrecio = BusquedaDivideVenceras.encontrarMaximoPrecio(productos, 0, productos.length - 1);
            System.out.println("   Precio máximo: $" + String.format("%.2f", maxPrecio));
            System.out.println();
            
            System.out.println("3. Contar Productos en Rango (Divide y Venceras):");
            int cantidad = BusquedaDivideVenceras.contarProductosEnRango(productos, 100, 500, 0, productos.length - 1);
            System.out.println("   Productos entre $100-$500: " + cantidad);
            System.out.println();
        }
        
        public void demostrarBacktracking() {
            System.out.println("=== BACKTRACKING ===");
            
            Producto[] productos = obtenerArray();
            
            System.out.println("1. Problema: Maximizar valor con presupuesto de $500");
            List<Producto> combo = BacktrackingInventario.encontrarComboPresupuesto(productos, 500);
            System.out.println("   Mejor combinacion encontrada:");
            double total = 0;
            for (Producto p : combo) {
                System.out.println("   - " + p);
                total += p.precio;
            }
            System.out.println("   Total: $" + String.format("%.2f", total));
            System.out.println("   Operaciones backtrack: " + operacionesBacktrack);
            System.out.println();
            
            System.out.println("2. Problema de la Mochila (peso máximo 5000g):");
            List<Producto> mochila = BacktrackingInventario.mochilaPeso(productos, 5000);
            System.out.println("   Mejor combinacion (maximo valor):");
            int pesoTotal = 0;
            double valorTotal = 0;
            for (Producto p : mochila) {
                System.out.println("   - " + p);
                pesoTotal += p.peso;
                valorTotal += p.precio;
            }
            System.out.println("   Peso total: " + pesoTotal + "g");
            System.out.println("   Valor total: $" + String.format("%.2f", valorTotal));
            System.out.println("   Operaciones backtrack: " + operacionesBacktrack);
            System.out.println();
            
            System.out.println("3. Encontrar combinaciones con categorias especificas:");
            String[] categoriasRequeridas = {"Gaming", "Audio", "Oficina"};
            List<List<Producto>> combinaciones = BacktrackingInventario.encontrarCombinacionesCategoria(
                productos, categoriasRequeridas);
            System.out.println("   Combinaciones encontradas: " + combinaciones.size());
            if (combinaciones.size() > 0) {
                System.out.println("   Primera combinacion:");
                for (Producto p : combinaciones.get(0)) {
                    System.out.println("   - " + p);
                }
            }
            System.out.println("   Operaciones backtrack: " + operacionesBacktrack);
            System.out.println();
        }
    }
    
    public static class GeneradorDatos {
        private static Random random = new Random();
        private static String[] nombres = {
            "Laptop Gaming", "Smartphone Pro", "Monitor 4K", "Teclado Mecanico", 
            "Mouse Gaming", "Audifonos Bluetooth", "Tablet Pro", "Impresora Laser"
        };
        
        private static String[] categorias = {"Computadoras", "Electronicos", "Gaming", "Audio", "Oficina"};
        
        public static Producto[] generarEjemplo() {
            return new Producto[] {
                new Producto(1001, "Laptop ASUS Gaming", 1299.99, 5, "Gaming", 2500),
                new Producto(1002, "iPhone 15 Pro", 999.99, 12, "Electronicos", 200),
                new Producto(1003, "Monitor LG 4K", 349.99, 8, "Computadoras", 5000),
                new Producto(1004, "Teclado Razer", 129.99, 25, "Gaming", 800),
                new Producto(1005, "Mouse Logitech", 79.99, 3, "Gaming", 100),
                new Producto(1006, "SSD Samsung 1TB", 89.99, 30, "Computadoras", 50),
                new Producto(1007, "AirPods Pro", 249.99, 7, "Audio", 50),
                new Producto(1008, "iPad Pro", 1099.99, 2, "Electronicos", 500),
                new Producto(1009, "Impresora HP", 199.99, 15, "Oficina", 3000),
                new Producto(1010, "Webcam Logitech", 89.99, 10, "Oficina", 150)
            };
        }
    }
    
    public static void main(String[] args) {
        System.out.println("SISTEMA AVANZADO DE INVENTARIO");
        System.out.println("Ordenacion + Hash + Backtracking + Divide y Venceras");
        System.out.println("=".repeat(55));
        System.out.println();
        
        GestorInventario gestor = new GestorInventario();
        Producto[] productos = GeneradorDatos.generarEjemplo();
        
        for (Producto p : productos) {
            gestor.agregar(p);
        }
        
        gestor.compararAlgoritmos();
        gestor.demostrarDivideVenceras();
        gestor.demostrarBacktracking();
        
        System.out.println("=== TECNICAS IMPLEMENTADAS ===");
        System.out.println("✓ MergeSort (Divide y Venceras): O(n log n)");
        System.out.println("✓ QuickSort (Divide y Venceras): O(n log n) promedio");
        System.out.println("✓ ShellSort: O(n log²n)");
        System.out.println("✓ Tablas Hash: O(1) busqueda");
        System.out.println("✓ Busqueda Binaria (Divide y Venceras): O(log n)");
        System.out.println("✓ Backtracking: Optimizacion combinatoria");
    }
}