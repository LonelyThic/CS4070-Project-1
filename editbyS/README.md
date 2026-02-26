Working in progress
in this folder. you can edit it or just edit the normal one. i made this for testing 

  ------------------------------------------------------------
  Code with comments
  ------------------------------------------------------------

1. Entity0:

    // Constructor: initialize distance table and send initial packets
    public Entity0()
    {
        System.out.println("Initializing Entity0");

        // Initialize distance table
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                distanceTable[i][j] = 999;

        // Set direct link costs
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            distanceTable[i][i] = NetworkSimulator.cost[0][i];

        printDT();

        // Send initial distance vector to neighbors
        sendUpdate();
    }


    //Update distance table when packet received
    public void update(Packet p)
    {
        int src = p.getSource();
        boolean updated = false;

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            int newCost =
                NetworkSimulator.cost[0][src] + p.getMincost(i);

            if(newCost < distanceTable[i][src])
            {
                distanceTable[i][src] = newCost;
                updated = true;
            }
        }

        if(updated)
        {
            System.out.println("Entity0 distance table updated");
            printDT();
            sendUpdate();
        }
    }

    //Compute minimum cost to each destination

    private int[] getMinCost()
    {
        int[] min = new int[NetworkSimulator.NUMENTITIES];

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            min[i] = 999;
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                min[i] = Math.min(min[i], distanceTable[i][j]);
        }
        return min;
    }

2. Entity 1 to 3 (they are same):

    public Entity1()
    {
        System.out.println("Initializing Entity1");

        // Initialize table with infinity
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            for(int j=0;j<NetworkSimulator.NUMENTITIES;j++)
                distanceTable[i][j]=999;

        // Set direct costs
        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
            distanceTable[i][i]=NetworkSimulator.cost[1][i];

        printDT();
        sendUpdate();
    }

    // Update table when packet arrives
    public void update(Packet p)
    {
        int src=p.getSource();
        boolean updated=false;

        for(int i=0;i<NetworkSimulator.NUMENTITIES;i++)
        {
            int cost=NetworkSimulator.cost[1][src]
                     +p.getMincost(i);

            if(cost<distanceTable[i][src])
            {
                distanceTable[i][src]=cost;
                updated=true;
            }
        }

        if(updated)
        {
            printDT();
            sendUpdate();
        }
    }

    // Compute minimum costs
    private int[] getMinCost()
    {
        int[] min=new int[NetworkSimulator.NUMENTITIES];

        for(int i=0;i<min.length;i++)
        {
            min[i]=999;
            for(int j=0;j<min.length;j++)
                min[i]=Math.min(min[i],distanceTable[i][j]);
        }
        return min;
    }

  ------------------------------------------------------------
  1. Overview
  ------------------------------------------------------------
  Each router in the network is represented as an Entity (Entity0 – Entity3).

  The simulator models how routers exchange routing information using distance vector updates until the network converges to optimal paths.
  ------------------------------------------------------------

2. Files Implemented / Modified

Entity0.java Entity1.java Entity2.java Entity3.java

Each entity represents a router node and performs:

• Distance table initialization • Sending initial distance vectors to
neighbors • Receiving routing updates • Updating routing tables using
Bellman‑Ford logic • Sending updated vectors when costs change

  ---------------------------------------
  3. Initialization Added (Constructor)
  ---------------------------------------

Inside each Entity constructor:

1.  The distance table is initialized.

2.  Direct link costs are copied from:
    NetworkSimulator.cost[entity][neighbor]

3.  Unknown paths are set to 999 (infinity).

4.  The router sends its initial distance vector to all neighbors using
    NetworkSimulator.toLayer2().

  ------------------------------------
  4. Update(Packet p) Implementation
  ------------------------------------

When a router receives a packet:

Step 1 – Identify sender int source = p.getSource();

Step 2 – Update distance table distanceTable[destination][source] =
cost_to_source + source_advertised_cost;

Step 3 – Apply Bellman‑Ford Algorithm For every destination: minimum
cost = min over all neighbors (cost_to_neighbor + neighbor_distance)

Step 4 – Detect Change If any minimum cost changes → routing table
updated.

Step 5 – Send New Distance Vector If update occurs, send new Packet to
all neighbors.

Purpose: This simulates distributed routing updates asynchronously.

  ----------------------------
  5. Bellman‑Ford Logic Used
  ----------------------------

NewCost(destination via neighbor) = Cost(this → neighbor) +
NeighborCost(neighbor → destination)

The router selects the minimum value among all neighbors.

  ------------------------------
  6. Packet Transmission Added
  ------------------------------

Packets are created using:

    new Packet(source, destination, mincost[])

Then sent via:

    NetworkSimulator.toLayer2(packet);

This simulates routing message exchange between routers.

  -----------------------
  7. Testing Procedure
  -----------------------

Required test inputs: Trace Level: 3 Link Change: 0 Random Seed: 1

Output saved using: java Project > Output.txt

  ------------
  8. Result
  ------------

each entity contains the optimal shortest path costs to every other entity in the network.

what am i doing... idk... help me...

  -----------------
  END OF DOCUMENT
  -----------------