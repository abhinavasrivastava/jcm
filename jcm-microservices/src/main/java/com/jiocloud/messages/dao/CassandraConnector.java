package com.jiocloud.messages.dao;

import static java.lang.System.out;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RoundRobinPolicy;
/**
 * Class used for connecting to Cassandra database.
 */
public class CassandraConnector
{
   /** Cassandra Cluster. */
   private Cluster cluster;
   /** Cassandra Session. */
   private Session session;
   /**
    * Connect to Cassandra Cluster specified by provided node IP
    * address and port number.
    *
    * @param node Cluster node IP address.
    * @param port Port of cluster host.
    */
   public void connect(final String node, final int port, final String keyspace)
   {
	   
	   

	   PoolingOptions poolingOptions = new PoolingOptions();
	   poolingOptions
	     // .setConnectionsPerHost(HostDistance.LOCAL,  4, 10)
	      //.setConnectionsPerHost(HostDistance.REMOTE, 2, 4);
	   .setCoreConnectionsPerHost(HostDistance.LOCAL,  2)
	   .setCoreConnectionsPerHost(HostDistance.REMOTE,  1);
	     // .setMaxRequestsPerConnection(HostDistance.LOCAL, 32768)
	     // .setMaxRequestsPerConnection(HostDistance.REMOTE, 2000);;
      this.cluster = Cluster.builder().addContactPoints(node.split(","))
    		  .withPort(port)
    		  .withProtocolVersion(ProtocolVersion.V3)
    		 // .withPoolingOptions(poolingOptions)
    		  //.withLoadBalancingPolicy(new RoundRobinPolicy())
    		  .build();
      
      
      
      final Metadata metadata = cluster.getMetadata();
      out.printf("Connected to cluster: %s\n", metadata.getClusterName());
      for (final Host host : metadata.getAllHosts())
      {
         out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
            host.getDatacenter(), host.getAddress(), host.getRack());
      }
      session = cluster.connect(keyspace);
     
   }
   /**
    * Provide my Session.
    *
    * @return My session.
    */
   public Session getSession()
   {
      return this.session;
   }
   /** Close cluster. */
   public void close()
   {
      cluster.close();
   }
}