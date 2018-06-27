package com.barclays.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.barclays.pojos.ConveyorLink;
import com.barclays.pojos.ConveyorNode;


public class ConveyorGraph implements Graph<ConveyorNode, ConveyorLink> {

    //adjacency list representation of graph
    private Map<ConveyorNode, List<ConveyorLink>> neighbors = new HashMap<ConveyorNode, List<ConveyorLink>>();

    private Set<ConveyorNode> nodes = new HashSet<ConveyorNode>();

    private Set<ConveyorLink> links = new HashSet<ConveyorLink>();

   
    public void addNode(ConveyorNode node) {
        if (!neighbors.containsKey(node)) {
            neighbors.put(node, new ArrayList<ConveyorLink>());
            nodes.add(node);
        }
    }

   
    public boolean isLink(ConveyorNode from, ConveyorNode to) {
        List<ConveyorLink> links = neighbors.get(from);
        if (links != null && !links.isEmpty()) {
            for (ConveyorLink conveyorLink : links) {
                if (conveyorLink.getTo().equals(to)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add a link to the Graph.
     *
     * @param from
     * @param to
     * @param cost
     */
    public void addLink(ConveyorNode from, ConveyorNode to, double cost) {
        addNode(from);
        addNode(to);

        if (!isLink(from, to)) {
            ConveyorLink link = new ConveyorLink(from, to, cost);
            neighbors.get(from).add(link);
            links.add(link);
        }
    }

    public ConveyorLink getLink(ConveyorNode source, ConveyorNode target) {
        List<ConveyorLink> links = neighbors.get(source);
        for (ConveyorLink link : links) {
            if (link.getTo().equals(target)) {
                return link;
            }
        }
        return null;
    }


    public Iterable<ConveyorNode> getNodes() {
        return nodes;
    }

    public int getOrder() {
        return neighbors.size();
    }

    public Iterable<ConveyorLink> getLinks() {
        return links;
    }

    public int getSize() {
        return links.size();
    }

    public boolean containsNode(ConveyorNode conveyorNode) {
        return neighbors.containsKey(conveyorNode);
    }

    public boolean containsLink(ConveyorLink conveyorLink) {
        return links.contains(conveyorLink);
    }

    public List<ConveyorNode> findShortestPath(ConveyorNode source, ConveyorNode target) {
        List<ConveyorNode> shortestPath = new ArrayList<ConveyorNode>();

        source.setMinDistance(0D);

        PriorityQueue<ConveyorNode> vertexQueue = new PriorityQueue<ConveyorNode>();

        for (ConveyorNode vertex : nodes) {
            if (!vertex.equals(source)) {
                vertex.setMinDistance(Double.POSITIVE_INFINITY);
                vertex.setPrevious(null);
            } else {
                vertex = source;
            }
            vertexQueue.add(vertex);
        }

        while (!vertexQueue.isEmpty()) {
            ConveyorNode u = vertexQueue.poll();

            if (u.equals(target)) {
                while (u.getPrevious() != null) {
                    shortestPath.add(u);
                    u = u.getPrevious();
                }
                break;
            }

            vertexQueue.remove(u);

            List<ConveyorLink> edges = neighbors.get(u);

            for (ConveyorLink edge : edges) {
                ConveyorNode v = edge.getTo();

                double weight = edge.getCost();
                double distanceThroughU = u.getMinDistance() + weight;

                if (distanceThroughU < v.getMinDistance()) {
                    v.setMinDistance(distanceThroughU);
                    v.setPrevious(u);
                    vertexQueue.remove(v);
                    vertexQueue.add(v);
                }
            }
        }

        Collections.reverse(shortestPath);

        return shortestPath;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ConveyorNode node : neighbors.keySet()) {
            sb.append("\n    " + node.getNodeId().getValue() + " -> " + neighbors.get(node));
        }
        return sb.toString();
    }

    public Map<ConveyorNode, List<ConveyorLink>> getNeighbors() {
        return neighbors;
    }

}
