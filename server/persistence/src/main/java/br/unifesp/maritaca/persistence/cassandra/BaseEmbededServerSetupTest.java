package br.unifesp.maritaca.persistence.cassandra;

import java.io.IOException;
import java.util.ArrayList;

import me.prettyprint.cassandra.connection.HConnectionManager;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.testutils.EmbeddedServerHelper;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.unifesp.maritaca.persistence.util.UtilsPersistence;

/**
 * Base class for test cases that need access to EmbeddedServerHelper
 *
 * @author Nate McCall (nate@vervewireless.com)
 *
 */
public abstract class BaseEmbededServerSetupTest {
  private static EmbeddedServerHelper embedded;
  private static Cluster              testCluster;
  
  protected HConnectionManager connectionManager;
  protected CassandraHostConfigurator cassandraHostConfigurator;
  protected String clusterName = "TestCluster";

  /**
   * Set embedded cassandra up and spawn it in a new thread.
   *
   * @throws TTransportException
   * @throws IOException
   * @throws InterruptedException
   */
  @BeforeClass
  public static void setup() throws TTransportException, IOException, InterruptedException, ConfigurationException {
	UtilsPersistence.loadConfigurationProperties("../business/src/test/resources/test-configuration.properties");
	  
	if (embedded == null){		  
	    embedded = new EmbeddedServerHelper();        
	    embedded.setup();	
	}
        
    testCluster = HFactory.getOrCreateCluster("TestCluster","localhost:9170");
    KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition("Maritaca",                 
          ThriftKsDef.DEF_STRATEGY_CLASS,  
          1, 
          new ArrayList<ColumnFamilyDefinition>());
    // Add the schema to the cluster.
    // "true" as the second param means that Hector will block until all nodes see the change.
    testCluster.addKeyspace(newKeyspace, true);
  }

  @AfterClass  
  public static void teardown() throws IOException {
	  testCluster.dropKeyspace("Maritaca", true);
  }
}
