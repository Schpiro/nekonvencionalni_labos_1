import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final Integer popSize = 10000;
    public static final Integer boardSize = 60;
    public static final Integer chanceToMutate = 50;
    public static final Random random = new Random();

    public static List<BoardState> genPop(int popSize, int boardSize)
    {
        List<BoardState> bs = new ArrayList<>();
        for(int i = 0; i < popSize; i++)
        {
            BoardState boardState = new BoardState(boardSize);
            bs.add(boardState);
        }
        return bs;
    }

    public static List<BoardState> recombination(BoardState bs1, BoardState bs2)
    {
        List<Integer> r1 = new ArrayList<>();
        List<Integer> r2 = new ArrayList<>();
        Integer nRekomb = random.nextInt(boardSize);
        for(int i = 0; i < nRekomb; i++)
        {
            r1.add(bs1.getArray().get(i));
            r2.add(bs2.getArray().get(i));
        }
        for(int i = nRekomb; i < bs2.getArray().size(); i++)
        {
            r1.add(bs2.getArray().get(i));
            r2.add(bs1.getArray().get(i));
        }

        List<BoardState> comb = new ArrayList<>();
        BoardState bs1_fin = new BoardState(r1);
        BoardState bs2_fin = new BoardState(r2);
        int mut = random.nextInt(100);
        if(mut <= chanceToMutate)
        {
            int mutGen = random.nextInt(boardSize);
            bs1_fin.getArray().set(mutGen, random.nextInt(boardSize));
        }
        mut = random.nextInt(100);
        if(mut <= chanceToMutate)
        {
            int mutGen = random.nextInt(boardSize);
            bs2_fin.getArray().set(mutGen, random.nextInt(boardSize));

        }
        comb.add(bs1_fin);
        comb.add(bs2_fin);
        return comb;
    }

    public static BoardState tournament(List<BoardState> boardStates){
        List<BoardState> bsCpy = new ArrayList<>(boardStates);

        int rand1 = random.nextInt(popSize);

        BoardState r1 = bsCpy.get(rand1);
        bsCpy.remove(rand1);
        int rand2 = random.nextInt(popSize-1);
        BoardState r2 = bsCpy.get(rand2);
        bsCpy.remove(rand2);
        int rand3 = random.nextInt(popSize-2);
        BoardState r3 = bsCpy.get(rand3);
        bsCpy.remove(rand3);

        List<BoardState> kandidati = new ArrayList<>();
        kandidati.add(r1);
        kandidati.add(r2);
        kandidati.add(r3);
        kandidati.sort((_r1, _r2) -> _r1.fitness().compareTo(_r2.fitness()));
        return kandidati.get(0);
    }

    public static List<BoardState> kTurnir(List<BoardState> boardStates)
    {
        List<BoardState> newGen = new ArrayList<>();

        for(int i = 0; i < popSize/2; i++)
        {
            List<BoardState> bsCpy = new ArrayList<>(boardStates);
            BoardState r1 = tournament(bsCpy);
            BoardState r2;
            do{
                r2 = tournament(bsCpy);
            }while ((r1.getArray()).equals(r2.getArray()));

            List<BoardState> child = new ArrayList<>(recombination(r1, r2));
            //System.out.println("Parents: \n"+r1 +" " +r2 +"Childs: \n" + child);
            newGen.add(child.get(0));
            newGen.add(child.get(1));
            boardStates.add(child.get(0));
            boardStates.add(child.get(1));
        }
        //System.out.println("MEDPOP:\n "+ medPop);
        boardStates.sort((bs1, bs2) -> bs1.fitness().compareTo(bs2.fitness()));
        //System.out.println(boardStates);
        boardStates.subList(popSize,popSize*2).clear();
        //System.out.println(boardStates);
        return newGen;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        List<BoardState> boardStates = new ArrayList<>();
        boardStates = genPop(popSize, boardSize);

        boardStates.sort((bs1, bs2) -> bs1.fitness().compareTo(bs2.fitness()));
        //System.out.print(boardStates + " ");
        for(int i = 0; i < boardStates.size(); i++)
        {
            System.out.print(boardStates.get(i).fitness() + " ");
        }
        System.out.println("");
        //System.out.println(recombination(2, boardStates.get(0), boardStates.get(1)));
        int count = 0;
        for(;;)
        {

            boardStates = kTurnir(boardStates);
            boardStates.sort((_bs1, _bs2) -> _bs1.fitness().compareTo(_bs2.fitness()));
            count++;
            System.out.println("new gen no: " + count);
            //System.out.println(boardStates);
            if(count == 4000)
            {
                break;
            }
            System.out.print(boardStates.get(0).fitness() + ",");
            if(boardStates.get(0).fitness().equals(0))
                break;
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;

        System.out.print("FINAL GEN\n" + boardStates);
        System.out.println("FINAL GEN NUMBER:" + count);
        System.out.println("TIME TO EXECUTE: " + duration + "ms");
    }
}