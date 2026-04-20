public class Entity1 extends Entity
{    
    // Constant representing infinity (no connection)
	static final int INFINITY = 999;
    // ID of this node (Entity1 → node 1)
    int id = 1;
    // Perform any necessary initialization in the constructor
    public Entity1()
    {
        // Debug message to confirm constructor is called
		System.out.println("Entity1 constructor called");

        //Initialize entire distance table to INFINITY
        // This means initially, no routes are known
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                distanceTable[i][j] = INFINITY;

        //Set direct link costs (from cost matrix)
        // distanceTable[i][i] = cost from this node to i
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            distanceTable[i][i] = NetworkSimulator.cost[id][i];

        sendToNeighbors(); // Send initial distance vector to neighbors
        printDT(); // Print initial distance table
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.

    // Called when a packet is received from a neighbor
    public void update(Packet p)
    {
        // Print which node sent the packet
		System.out.println("Entity1 receives packet from "+p.getSource());

        // Track if table changes
        boolean updated = false;
         // Neighbor who sent packet
        int src = p.getSource();

        //Loop through all possible destinations
        for(int dest=0; dest<NetworkSimulator.NUMENTITIES; dest++)
        {
            //Apply Bellman-Ford equation
            // cost to dest via src =
            // cost to src + src's cost to dest
            int newCost =
                NetworkSimulator.cost[id][src] +
                p.getMincost(dest);

                //If new path is better, update table
            if(newCost < distanceTable[dest][src])
            {
                distanceTable[dest][src] = newCost;
                updated = true;
            }
        }
        //If any update occurred, notify neighbors
        if(updated)
        {
            sendToNeighbors();  // Send updated distance vector to neighbors
            printDT(); // Print updated distance table
        }
    }

    // Computes the minimum cost to each destination based on the current distance table
	private int[] getMinCost()
    {
        int[] min = new int[NetworkSimulator.NUMENTITIES];

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            min[i]=INFINITY;
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                min[i]=Math.min(min[i],distanceTable[i][j]);
        }
        return min;
    }

     // Sends current distance vector to all directly connected neighbors
	private void sendToNeighbors()
    {
        // Get best known costs to all destinations
        int[] mincost = getMinCost();

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            // Send only if:
            // - not sending to itself
            // - link exists (cost not infinity)
            if(i!=id && NetworkSimulator.cost[id][i]!=INFINITY)
                NetworkSimulator.toLayer2(new Packet(id,i,mincost));
        }
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    // Prints the distance table in a formatted way
   public void printDT()
    {
        		System.out.println();
		        System.out.println("           via");
		        System.out.println(" D1 |  0   1   2   3");
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
