import java.util.*;

public class SistemaInventarioCompleto {
    
    private static long comparacionesSort = 0;
    private static long operacionesHash = 0;
    
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
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Producto producto = (Producto) obj;
            return id == producto.id;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }
    
    public static class MergeSort {
        
        public static void ordenarPorPrecio(Producto[] productos) {
            comparacionesSort = 0;
            mergeSortPrecio(productos, 0, productos.length - 1);
        }
        
        public static void ordenarPorStock(Producto[] productos) {
            comparacionesSort = 0;
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
                comparacionesSort++;
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
                comparacionesSort++;
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
    
    public static class TablaHashInventario {
        private HashMap<Integer, Producto> tablaPorId;
        private HashMap<String, List<Producto>> tablaPorCategoria;
        private HashMap<String, List<Producto>> tablaPorNombre;
        private HashMap<Integer, List<Producto>> tablaPorRangoPrecio;
        
        public TablaHashInventario() {
            tablaPorId = new HashMap<>();
            tablaPorCategoria = new HashMap<>();
            tablaPorNombre = new HashMap<>();
            tablaPorRangoPrecio = new HashMap<>();
        }
        
        public void insertar(Producto producto) {
            operacionesHash++;
            tablaPorId.put(producto.id, producto);
            
            operacionesHash++;
            tablaPorCategoria.computeIfAbsent(producto.categoria, k -> new ArrayList<>()).add(producto);
            
            operacionesHash++;
            String palabraClave = producto.nombre.toLowerCase().split(" ")[0];
            tablaPorNombre.computeIfAbsent(palabraClave, k -> new ArrayList<>()).add(producto);
            
            operacionesHash++;
            int rangoPrecio = (int)(producto.precio / 100) * 100;
            tablaPorRangoPrecio.computeIfAbsent(rangoPrecio, k -> new ArrayList<>()).add(producto);
        }
        
        public Producto buscarPorId(int id) {
            operacionesHash++;
            return tablaPorId.get(id);
        }
        
        public List<Producto> buscarPorCategoria(String categoria) {
            operacionesHash++;
            return tablaPorCategoria.getOrDefault(categoria, new ArrayList<>());
        }
        
        public List<Producto> buscarPorNombre(String palabraClave) {
            operacionesHash++;
            return tablaPorNombre.getOrDefault(palabraClave.toLowerCase(), new ArrayList<>());
        }
        
        public List<Producto> buscarPorRangoPrecio(double precioMin, double precioMax) {
            List<Producto> resultado = new ArrayList<>();
            
            for (int rango = (int)(precioMin / 100) * 100; rango <= precioMax; rango += 100) {
                operacionesHash++;
                List<Producto> productosRango = tablaPorRangoPrecio.get(rango);
                if (productosRango != null) {
                    for (Producto p : productosRango) {
                        if (p.precio >= precioMin && p.precio <= precioMax) {
                            resultado.add(p);
                        }
                    }
                }
            }
            return resultado;
        }
        
        public boolean eliminar(int id) {
            operacionesHash++;
            Producto producto = tablaPorId.remove(id);
            
            if (producto != null) {
                operacionesHash++;
                List<Producto> categoria = tablaPorCategoria.get(producto.categoria);
                if (categoria != null) categoria.remove(producto);
                
                operacionesHash++;
                String palabraClave = producto.nombre.toLowerCase().split(" ")[0];
                List<Producto> nombre = tablaPorNombre.get(palabraClave);
                if (nombre != null) nombre.remove(producto);
                
                operacionesHash++;
                int rangoPrecio = (int)(producto.precio / 100) * 100;
                List<Producto> precio = tablaPorRangoPrecio.get(rangoPrecio);
                if (precio != null) precio.remove(producto);
                
                return true;
            }
            return false;
        }
        
        public void actualizar(Producto producto) {
            if (tablaPorId.containsKey(producto.id)) {
                eliminar(producto.id);
                insertar(producto);
            }
        }
        
        public int getTamaño() {
            return tablaPorId.size();
        }
        
        public void mostrarEstadisticas() {
            System.out.println("=== ESTADÍSTICAS TABLA HASH ===");
            System.out.println("Total productos: " + tablaPorId.size());
            System.out.println("Categorías diferentes: " + tablaPorCategoria.size());
            System.out.println("Palabras clave diferentes: " + tablaPorNombre.size());
            System.out.println("Rangos de precio: " + tablaPorRangoPrecio.size());
            System.out.println("Operaciones hash realizadas: " + operacionesHash);
            System.out.println();
        }
    }
    
    public static class GestorInventario {
        private List<Producto> inventario;
        private TablaHashInventario tablaHash;
        
        public GestorInventario() {
            inventario = new ArrayList<>();
            tablaHash = new TablaHashInventario();
        }
        
        public void agregar(Producto producto) {
            inventario.add(producto);
            tablaHash.insertar(producto);
        }
        
        public Producto[] obtenerArray() {
            return inventario.toArray(new Producto[0]);
        }
        
        public void mostrar(String titulo, Producto[] productos, int limite) {
            System.out.println("=== " + titulo + " ===");
            for (int i = 0; i < Math.min(productos.length, limite); i++) {
                System.out.println((i + 1) + ". " + productos[i]);
            }
            System.out.println("Comparaciones MergeSort: " + comparacionesSort);
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
        
        public Producto buscarPorId(int id) {
            operacionesHash = 0;
            return tablaHash.buscarPorId(id);
        }
        
        public List<Producto> buscarPorCategoria(String categoria) {
            operacionesHash = 0;
            return tablaHash.buscarPorCategoria(categoria);
        }
        
        public List<Producto> buscarPorNombre(String nombre) {
            operacionesHash = 0;
            return tablaHash.buscarPorNombre(nombre);
        }
        
        public List<Producto> buscarPorRangoPrecio(double min, double max) {
            operacionesHash = 0;
            return tablaHash.buscarPorRangoPrecio(min, max);
        }
        
        public boolean eliminarProducto(int id) {
            operacionesHash = 0;
            Producto producto = tablaHash.buscarPorId(id);
            if (producto != null) {
                inventario.remove(producto);
                return tablaHash.eliminar(id);
            }
            return false;
        }
        
        public void generarReporte() {
            System.out.println("=== REPORTE INTEGRADO ===");
            
            Producto[] bajoStock = obtenerBajoStock(10);
            System.out.println("PRODUCTOS CON BAJO STOCK (< 10): " + bajoStock.length);
            for (int i = 0; i < Math.min(5, bajoStock.length); i++) {
                System.out.println("⚠️  " + bajoStock[i]);
            }
            System.out.println();
            
            Producto[] masCaros = obtenerMasCaros(3);
            System.out.println("TOP 3 PRODUCTOS MÁS CAROS:");
            for (Producto p : masCaros) {
                System.out.println("💰 " + p);
            }
            System.out.println();
            
            tablaHash.mostrarEstadisticas();
        }
        
        public void demostrarBusquedas() {
            System.out.println("=== DEMOSTRACIÓN DE BÚSQUEDAS HASH ===");
            
            System.out.println("1. BÚSQUEDA POR ID (O(1)):");
            Producto p = buscarPorId(1005);
            if (p != null) {
                System.out.println("   Encontrado: " + p);
            } else {
                System.out.println("   No encontrado");
            }
            System.out.println("   Operaciones hash: " + operacionesHash);
            System.out.println();
            
            System.out.println("2. BÚSQUEDA POR CATEGORÍA (O(1)):");
            List<Producto> gaming = buscarPorCategoria("Gaming");
            System.out.println("   Productos Gaming encontrados: " + gaming.size());
            for (int i = 0; i < Math.min(3, gaming.size()); i++) {
                System.out.println("   - " + gaming.get(i));
            }
            System.out.println("   Operaciones hash: " + operacionesHash);
            System.out.println();
            
            System.out.println("3. BÚSQUEDA POR NOMBRE (O(1)):");
            List<Producto> laptops = buscarPorNombre("laptop");
            System.out.println("   Productos 'laptop' encontrados: " + laptops.size());
            for (int i = 0; i < Math.min(3, laptops.size()); i++) {
                System.out.println("   - " + laptops.get(i));
            }
            System.out.println("   Operaciones hash: " + operacionesHash);
            System.out.println();
            
            System.out.println("4. BÚSQUEDA POR RANGO DE PRECIO:");
            List<Producto> rango = buscarPorRangoPrecio(100, 300);
            System.out.println("   Productos entre $100-$300: " + rango.size());
            for (int i = 0; i < Math.min(3, rango.size()); i++) {
                System.out.println("   - " + rango.get(i));
            }
            System.out.println("   Operaciones hash: " + operacionesHash);
            System.out.println();
        }
    }
    
    public static class GeneradorDatos {
        private static Random random = new Random();
        private static String[] nombres = {
            "Laptop Gaming", "Smartphone Pro", "Monitor 4K", "Teclado Mecánico", 
            "Mouse Gaming", "Audífonos Bluetooth", "Tablet Pro", "Cámara Digital",
            "Impresora Laser", "Disco SSD", "Memoria RAM", "Tarjeta Gráfica"
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
                new Producto(1001, "Laptop ASUS Gaming", 1299.99, 5, "Gaming"),
                new Producto(1002, "iPhone 15 Pro", 999.99, 12, "Electrónicos"),
                new Producto(1003, "Monitor LG 4K", 349.99, 8, "Computadoras"),
                new Producto(1004, "Teclado Razer", 129.99, 25, "Gaming"),
                new Producto(1005, "Mouse Logitech", 79.99, 3, "Gaming"),
                new Producto(1006, "SSD Samsung 1TB", 89.99, 30, "Computadoras"),
                new Producto(1007, "AirPods Pro", 249.99, 7, "Audio"),
                new Producto(1008, "iPad Pro", 1099.99, 2, "Electrónicos"),
                new Producto(1009, "Impresora HP", 199.99, 15, "Oficina"),
                new Producto(1010, "Cámara Canon", 799.99, 1, "Electrónicos"),
                new Producto(1011, "Laptop Dell Business", 899.99, 6, "Computadoras"),
                new Producto(1012, "Mouse Gaming RGB", 59.99, 20, "Gaming")
            };
        }
    }
    
    public static void compararRendimiento() {
        System.out.println("=== COMPARACIÓN: MERGESORT vs HASH ===");
        
        GestorInventario gestor = new GestorInventario();
        Producto[] productos = GeneradorDatos.generar(10000);
        
        for (Producto p : productos) {
            gestor.agregar(p);
        }
        
        System.out.println("Inventario con 10,000 productos creado");
        System.out.println();
        
        System.out.println("ESCENARIO 1: Buscar producto específico por ID");
        long inicio = System.nanoTime();
        Producto encontrado = gestor.buscarPorId(5000);
        long tiempoHash = System.nanoTime() - inicio;
        
        inicio = System.nanoTime();
        Producto[] todos = gestor.obtenerArray();
        for (Producto p : todos) {
            if (p.id == 5000) {
                encontrado = p;
                break;
            }
        }
        long tiempoBusquedaLineal = System.nanoTime() - inicio;
        
        System.out.printf("Búsqueda Hash:   %.3f ms (O(1))\n", tiempoHash / 1_000_000.0);
        System.out.printf("Búsqueda Lineal: %.3f ms (O(n))\n", tiempoBusquedaLineal / 1_000_000.0);
        System.out.printf("Hash es %.1fx más rápido\n", (double)tiempoBusquedaLineal / tiempoHash);
        System.out.println();
        
        System.out.println("ESCENARIO 2: Obtener top 10 productos más caros");
        inicio = System.nanoTime();
        Producto[] topCaros = gestor.obtenerMasCaros(10);
        long tiempoMergeSort = System.nanoTime() - inicio;
        
        System.out.printf("MergeSort + filtrado: %.3f ms (O(n log n))\n", tiempoMergeSort / 1_000_000.0);
        System.out.printf("Comparaciones realizadas: %d\n", comparacionesSort);
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("SISTEMA INVENTARIO: MERGESORT + TABLAS HASH");
        System.out.println("=".repeat(45));
        System.out.println();
        
        GestorInventario gestor = new GestorInventario();
        Producto[] productos = GeneradorDatos.generarEjemplo();
        
        System.out.println("Cargando inventario con " + productos.length + " productos...");
        for (Producto p : productos) {
            gestor.agregar(p);
        }
        System.out.println();
        
        System.out.println("=== FUNCIONALIDAD MERGESORT ===");
        Producto[] porPrecio = gestor.obtenerArray();
        MergeSort.ordenarPorPrecio(porPrecio);
        gestor.mostrar("PRODUCTOS ORDENADOS POR PRECIO", porPrecio, 8);
        
        gestor.demostrarBusquedas();
        
        gestor.generarReporte();
        
        compararRendimiento();
        
        System.out.println("=== VENTAJAS DE LA COMBINACIÓN ===");
        System.out.println("✓ MergeSort: Ordenación eficiente O(n log n)");
        System.out.println("✓ Hash Tables: Búsquedas ultra-rápidas O(1)");
        System.out.println("✓ Combinación perfecta para sistemas reales");
        System.out.println("✓ MergeSort para análisis, Hash para consultas");
    }
}