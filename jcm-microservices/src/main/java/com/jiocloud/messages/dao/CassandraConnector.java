package com.jiocloud.messages.dao;

import static java.lang.System.out;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
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
	     .setConnectionsPerHost(HostDistance.LOCAL,  1, 1)
	      //.setConnectionsPerHost(HostDistance.REMOTE, 2, 4);
	   //.setCoreConnectionsPerHost(HostDistance.LOCAL,  2)
	   //.setCoreConnectionsPerHost(HostDistance.REMOTE,  1)
	      .setMaxRequestsPerConnection(HostDistance.LOCAL, 32768)
	      .setMaxRequestsPerConnection(HostDistance.REMOTE, 2000);
      this.cluster = Cluster.builder().addContactPoints(node.split(","))
    		  .withPort(port)
    		  .withProtocolVersion(ProtocolVersion.V4)
    		  //.withPoolingOptions(poolingOptions)
    		  //.withLoadBalancingPolicy(new RoundRobinPolicy())
    		  .build();
      cluster.init();
      
      
      final Metadata metadata = cluster.getMetadata();
      out.printf("Connected to cluster: %s\n", metadata.getClusterName());
      for (final Host host : metadata.getAllHosts())
      {
         out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
            host.getDatacenter(), host.getAddress(), host.getRack());
      }
      session = cluster.connect(keyspace);
      
      
      
      final LoadBalancingPolicy loadBalancingPolicy =
    		    cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
    		final PoolingOptions po =
    		cluster.getConfiguration().getPoolingOptions();

    		ScheduledExecutorService scheduled =
    		Executors.newScheduledThreadPool(1);
    		scheduled.scheduleAtFixedRate(new Runnable() {
    		    @Override
    		    public void run() {
    		        Session.State state = session.getState();
    		        for (Host host : state.getConnectedHosts()) {
    		            HostDistance distance = loadBalancingPolicy.distance(host);
    		            int connections = state.getOpenConnections(host);
    		            int inFlightQueries = state.getInFlightQueries(host);
//    		            System.out.printf("%s connections=%d, current load=%d, max
//    		load=%d%n",
//    		                host, connections, inFlightQueries,
//    		                connections *
//    		poolingOptions.getMaxRequestsPerConnection(distance));
    		            
    		           System.out.println(host + "- connections=" + connections + ", current load=" + inFlightQueries + ", max load=" + connections *
    		po.getMaxRequestsPerConnection(distance));
    		        }
    		    }
    		}, 5, 5, TimeUnit.SECONDS);
     
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