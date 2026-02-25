public class Entity2 extends Entity
{    
    private static final int INFINITY = 999;
    private int id = 2;

    // Perform any necessary initialization in the constructor
    public Entity2()
    {
        System.out.println("Entity2 constructor called");

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                distanceTable[i][j]=INFINITY;

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            distanceTable[i][i]=NetworkSimulator.cost[id][i];

        sendToNeighbors();
        printDT();
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {
        System.out.println("Entity2 receives packet from "+p.getSource());

        boolean updated=false;
        int src=p.getSource();

        for(int dest=0;dest<NetworkSimulator.NUMENTITIES;dest++)
        {
            int newCost=
                NetworkSimulator.cost[id][src]+
                p.getMincost(dest);

            if(newCost<distanceTable[dest][src])
            {
                distanceTable[dest][src]=newCost;
                updated=true;
            }
        }

        if(updated)
        {
            sendToNeighbors();
            printDT();
        }
    }

    private int[] getMinCost()
    {
        int[] min=new int[NetworkSimulator.NUMENTITIES];

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            min[i]=INFINITY;
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                min[i]=Math.min(min[i],distanceTable[i][j]);
        }
        return min;
    }

    private void sendToNeighbors()
    {
        int[] mincost=getMinCost();

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            if(i!=id && NetworkSimulator.cost[id][i]!=INFINITY)
                NetworkSimulator.toLayer2(new Packet(id,i,mincost));
        }
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
      {
          System.out.println();
          System.out.println("           via");
          System.out.println(" D2 |  0   1   2   3");
          System.out.println("----+-----------------");
          for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
          {
              System.out.print("   " + i + "|");
              for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
              {
                  if (distanceTable[i][j] < 10)
                  {
                      System.out.print("   ");
                  }
                  else if (distanceTable[i][j] < 100)
                  {
                      System.out.print("  ");
                  }
                  else
                  {
                      System.out.print(" ");
                  }

                  System.out.print(distanceTable[i][j]);
              }
              System.out.println();
          }
      }
}
