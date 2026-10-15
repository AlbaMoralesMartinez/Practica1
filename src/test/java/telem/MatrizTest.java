package telem;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;

import telem.Matriz;


/**
 * Pruebas de los constructores de Matriz.
 */
public class MatrizTest {

    /*
     * Comprobamos el caso más sencillo:
     * el constructor recibe una matriz rectangular.
     */
    @Test
    public void constructorMatrizRectangular() {

        double[][] datos = {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0, 6.0 }
        };

        Matriz m = new Matriz(datos);

        assertArrayEquals(
                new int[] { 2, 3 },
                m.getDimension());

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(datos[i][j], m.getElemento(i, j), 1e-9);
            }
        }        
    }

    /*
     * Si las filas no tienen la misma longitud,
     * la matriz toma como número de columnas la longitud
     * de la fila más larga y completa con ceros.
     */
    @Test
    public void constructorRellenaConCeros() {

        double[][] datos = {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0 },
            { 6.0 },
            { }
        };

        Matriz m = new Matriz(datos);

        assertArrayEquals(
                new int[] { 4, 3 },
                m.getDimension());

        assertEquals(4.0, m.getElemento(1, 0), 1e-9);
        assertEquals(5.0, m.getElemento(1, 1), 1e-9);
        assertEquals(0.0, m.getElemento(1, 2), 1e-9);

        assertEquals(6.0, m.getElemento(2, 0), 1e-9);
        assertEquals(0.0, m.getElemento(2, 1), 1e-9);
        assertEquals(0.0, m.getElemento(2, 2), 1e-9);
        assertEquals(0.0, m.getElemento(3, 0), 1e-9);
        assertEquals(0.0, m.getElemento(3, 1), 1e-9);
        assertEquals(0.0, m.getElemento(3, 2), 1e-9);
    }

    /*
     * El constructor debe copiar los datos recibidos.
     * Modificar posteriormente el array original no debe
     * modificar la matriz.
     */
    @Test
    public void constructorHaceUnaCopiaDeLosDatos() {

        double[][] datos = {
            { 1.0, 2.0 },
            { 3.0, 4.0 }
        };

        Matriz m = new Matriz(datos);

        datos[0][0] = 100.0;

        assertEquals(1.0, m.getElemento(0, 0), 1e-9);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array null o vacío.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizNull() {
        new Matriz(null);
    }  
    
    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array vacío.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizVacia() {
        new Matriz(new double[0][0]);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * todos los arrays con filas vacías.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizConFilasVacias() {
        double[][] datos = {
            { },
            { }
        };

        new Matriz(datos);
    }

    /*
     * Comprobamos que el constructor lanza una excepción si recibe
     * un array con filas null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMatrizConFilasNull() {
        double[][] datos = {
            { 1.0, 2.0 },
            null
        };

        new Matriz(datos);
    }

    @Test
    public void getDimensionDevuelveUnaCopia() {
        Matriz matriz = new Matriz(new double[][] { { 1.0, 2.0 } });

        int[] dimensiones = matriz.getDimension();
        dimensiones[0] = 99;

        assertArrayEquals(new int[] { 1, 2 }, matriz.getDimension());
    }

    @Test
    public void sumarDevuelveUnaNuevaMatrizConLaSuma() {
        Matriz primera = new Matriz(new double[][] {
            { 1.0, 2.0 },
            { 3.0, 4.0 }
        });
        Matriz segunda = new Matriz(new double[][] {
            { 5.0, 6.0 },
            { 7.0, 8.0 }
        });

        Matriz resultado = primera.sumar(segunda);

        assertArrayEquals(new int[] { 2, 2 }, resultado.getDimension());
        assertEquals(6.0, resultado.getElemento(0, 0), 1e-9);
        assertEquals(8.0, resultado.getElemento(0, 1), 1e-9);
        assertEquals(10.0, resultado.getElemento(1, 0), 1e-9);
        assertEquals(12.0, resultado.getElemento(1, 1), 1e-9);
        assertEquals(1.0, primera.getElemento(0, 0), 1e-9);
    }

    @Test
    public void sumarDevuelveNullSiLaOtraMatrizEsNull() {
        Matriz matriz = new Matriz(new double[][] { { 1.0 } });

        assertEquals(null, matriz.sumar(null));
    }

    @Test
    public void sumarDevuelveNullSiLasDimensionesSonDistintas() {
        Matriz matriz = new Matriz(new double[][] { { 1.0, 2.0 } });
        Matriz otra = new Matriz(new double[][] {
            { 3.0 },
            { 4.0 }
        });

        assertEquals(null, matriz.sumar(otra));
    }

    @Test
    public void getElementoDevuelveElValorEnLosLimites() {
        Matriz matriz = new Matriz(new double[][] {
            { 1.5, 2.5 },
            { 3.5, 4.5 }
        });

        assertEquals(1.5, matriz.getElemento(0, 0), 1e-9);
        assertEquals(4.5, matriz.getElemento(1, 1), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getElementoRechazaFilaNegativa() {
        new Matriz(new double[][] { { 1.0 } }).getElemento(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getElementoRechazaFilaFueraDeRango() {
        new Matriz(new double[][] { { 1.0 } }).getElemento(1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getElementoRechazaColumnaNegativa() {
        new Matriz(new double[][] { { 1.0 } }).getElemento(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getElementoRechazaColumnaFueraDeRango() {
        new Matriz(new double[][] { { 1.0 } }).getElemento(0, 1);
    }

    @Test
    public void toStringRepresentaFilasYColumnas() {
        Matriz matriz = new Matriz(new double[][] {
            { 1.0, 2.0 },
            { 3.0, 4.0 }
        });
        String separador = System.lineSeparator();

        assertEquals("[1.0, 2.0]" + separador + "[3.0, 4.0]", matriz.toString());
    }

    @Test
    public void mainImprimeLasDosMatricesDeEjemplo() {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;
        String separador = System.lineSeparator();

        try {
            System.setOut(new PrintStream(salida));
            Matriz.main(new String[0]);
        } finally {
            System.setOut(salidaOriginal);
        }

        String esperado = "Dimensiones: 3x3" + separador
                + "Matriz:" + separador
                + "[1.0, 2.0, 3.0]" + separador
                + "[4.0, 5.0, 0.0]" + separador
                + "[6.0, 0.0, 0.0]" + separador
                + "Dimensiones: 2x2" + separador
                + "Matriz:" + separador
                + "[1.0, 2.0]" + separador
                + "[0.0, 0.0]" + separador;

        assertEquals(esperado, salida.toString());
    }
}
