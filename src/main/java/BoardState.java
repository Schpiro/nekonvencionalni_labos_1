import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardState {
    private List<Integer> array = new ArrayList<>();
    private int boardSize;
    private int fitness;
    Random rand = new Random();

    public BoardState(int boardSize) {
        this.boardSize = boardSize;
        for(int i = 0; i < boardSize; i++)
        {
            array.add(rand.nextInt(boardSize));
        }
       fitness = fitness();
    }
    public BoardState(List<Integer> array)
    {
        this.array = array;
        this.boardSize = array.size();
        fitness = fitness();
    }

    public List<Integer> getArray() {
        return array;
    }
    public void setArray(List<Integer> array) {
        this.array = array;
    }
    public Integer fitness()
    {
        Integer fitnessResult = 0;
        for(int i = 0; i < array.toArray().length; i++)
        {
            for(int j = i; j < array.toArray().length; j++)
            {
                if(i == j) continue;
                else
                {
                    if(array.get(i).equals(array.get(j))) {
                        fitnessResult += 1;
                    }
                    int pozDijagonalaI = array.get(i)+i;
                    int pozDijagonalaJ = array.get(j)+j;
                    int negDijagonalaI = i-array.get(i);
                    int negDijagonalaj = j-array.get(j);


                    if(pozDijagonalaI == pozDijagonalaJ || negDijagonalaI == negDijagonalaj)
                    {
                        fitnessResult += 1;
                    }
                }
            }
        }
        return fitnessResult;
    }

    @Override
    public String toString() {
        return array + "\n";
    }
}
