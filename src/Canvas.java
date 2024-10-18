import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private final static int STROKE_SIZE = 8;

    // Mantener todos los recorridos creados en canva
    private List<List<ColorPoint>> allPaths;

    // Dibujar la linea entre ambos puntos
    private List<ColorPoint> currentPath;

    // Color de los puntos
    private Color color;

    // Propiedades de tamaño del canva
    private int canvasWidth, canvasHeight;

    // Ubicación de los puntos
    private int x, y;

    public Canvas(int targetWidth, int targetHeight) {
        super();
        setPreferredSize(new Dimension(targetWidth, targetHeight));
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Inicialización de Variables
        allPaths = new ArrayList<>(25);
        canvasWidth = targetWidth;
        canvasHeight = targetHeight;

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Ubicación Actual del Mouse
                x = e.getX();
                y = e.getY();

                // Dibujar en donde esté el mouse
                Graphics g = getGraphics();
                g.setColor(color);
                g.fillRect(x, y, STROKE_SIZE, STROKE_SIZE);
                g.dispose();

                // Iniciar la ruta actual
                currentPath = new ArrayList<>(25);
                currentPath.add(new ColorPoint(x, y, color));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Agregar los caminos recorridos a la lista
                allPaths.add(currentPath);

                // Reiniciar la ruta recorrida
                currentPath = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Obtener la ubicacion actual
                x = e.getX();
                y = e.getY();

                // Permite crear la linea del dibujo
                Graphics2D g2d = (Graphics2D) getGraphics();
                g2d.setColor(color);
                if (!currentPath.isEmpty()) {
                    ColorPoint prevPoint = currentPath.get(currentPath.size() - 1);
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));

                    // Conectar los puntos para hacer la linea
                    g2d.drawLine(prevPoint.getX(), prevPoint.getY(), x, y);
                }
                g2d.dispose();

                // Agregar el nuevo punto a la ruta
                ColorPoint nextPoint = new ColorPoint(e.getX(), e.getY(), color);
                currentPath.add(nextPoint);
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);

    }

    public void setColor(Color color){
        this.color = color;
    }

    public void resetCanvas(){
        // Limpiar pizarra
        Graphics g = getGraphics();
        g.clearRect(0, 0, canvasWidth, canvasHeight);
        g.dispose();

        // Reiniciar el recorrido
        allPaths = new ArrayList<>(25);
        currentPath = null;

        repaint();
        revalidate();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // ReDibujar todos los dibujos creados
        for (List<ColorPoint> path : allPaths) {
            ColorPoint from = null;
            for (ColorPoint point : path) {
                g2d.setColor(point.getColor());

                // En caso de cuando se crea solo un punto
                if (path.size() == 1){
                    g2d.fillRect(point.getX(), point.getY(), STROKE_SIZE, STROKE_SIZE);
                }

                if (from != null) {
                    g2d.setStroke(new BasicStroke(STROKE_SIZE));
                    g2d.drawLine(from.getX(), from.getY(), point.getX(), point.getY());
                }
                from = point;
            }
        }
    }

}
