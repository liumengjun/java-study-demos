package gui.javafx.chart;

/**
 * java9+
 * 不能直接启动JavaFX App，会出现“错误: 缺少 JavaFX 运行时组件, 需要使用该组件来运行此应用程序”。
 * 使用一种引导类来代理启动 JavaFX 应用。
 * Created by liumengjun on 2023-11-02.
 */
public class JavaFXBootsrrap {
    public static void main(String[] args) {
        PieChartSample.main(args);
//        PieChartImageFileSample.main(args);
    }
}
