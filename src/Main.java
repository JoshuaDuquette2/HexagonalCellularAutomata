import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private final int GRID_HEIGHT = 40;
    private final int GRID_WIDTH  = 60;
    private final double FILL_PERCENTAGE_1 = 0.7;
    private final double FILL_PERCENTAGE_2 = 0.9;
    private final int GENERATIONS = 100;

    private Hex[][] currentBuffer = new Hex[GRID_WIDTH][GRID_HEIGHT];
    private Hex[][] nextBuffer = new Hex[GRID_WIDTH][GRID_HEIGHT];

    private int[][] dirs = new int[][]{{-1,0}, {0,-1}, {1,0}, {1,1}, {0,1}, {-1,1}};

    @Override
    public void start(Stage primaryStage){
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setScene(scene);

        for(int x = 0; x < GRID_WIDTH; x++){
            for(int y = 0; y < GRID_HEIGHT; y++){
                Hex hex = new Hex(x,y);

                double stateChance = Math.random();
                if(stateChance >= FILL_PERCENTAGE_1){
                    hex.setState(stateChance >= FILL_PERCENTAGE_2 ? 2 : 1);
                }

                currentBuffer[x][y] = hex;
                root.getChildren().add(hex);
            }
        }
        primaryStage.show();
        runGame();
    }

    public synchronized void runGame(){
        System.out.println("Running.");

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            //Update next buffer with information from the current buffer
                            for(int x = 0; x < GRID_WIDTH; x++){
                                for(int y = 0; y < GRID_HEIGHT; y++){
                                    int stateUpdate = evaluateNextState(x, y);
                                    Hex hex = currentBuffer[x][y];
                                    hex.setState(stateUpdate);
                                    nextBuffer[x][y] = hex;
                                }
                            }
                            //Update the buffer
                            currentBuffer = nextBuffer;
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);

        for(int i = 0; i < GENERATIONS; i++){
            timeline.play();
        }
    }

    private int evaluateNextState(int x, int y){
        int neighbors = 0;
        for(int[] dir : dirs){
            try{
                neighbors += currentBuffer[x+dir[0]][y+dir[1]].state;
            } catch(Exception e){}
        }
        int out = 0;
        if(currentBuffer[x][y].state == 0 && neighbors == 4){
            out = 1;
        } else if(currentBuffer[x][y].state == 1 && ((neighbors >= 1 && neighbors <= 4) || neighbors == 6)){
            out = 2;
        } else if(currentBuffer[x][y].state == 2 && (neighbors == 1 || neighbors == 2)){
            out = 2;
        } else if(currentBuffer[x][y].state == 2 && neighbors == 4){
            out = 1;
        }
        return out;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
