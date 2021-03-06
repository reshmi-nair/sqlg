package org.umlg.sqlg.test.usersuppliedpk.topology;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.umlg.sqlg.structure.PropertyType;
import org.umlg.sqlg.structure.SqlgGraph;
import org.umlg.sqlg.structure.topology.EdgeLabel;
import org.umlg.sqlg.structure.topology.VertexLabel;
import org.umlg.sqlg.test.BaseTest;

import java.net.URL;
import java.util.*;

/**
 * @author Pieter Martin (https://github.com/pietermartin)
 * Date: 2018/03/17
 */
public class TestUserSuppliedPKTopology extends BaseTest {

    @SuppressWarnings("Duplicates")
    @BeforeClass
    public static void beforeClass() {
        URL sqlProperties = Thread.currentThread().getContextClassLoader().getResource("sqlg.properties");
        try {
            configuration = new PropertiesConfiguration(sqlProperties);
            if (isPostgres()) {
                configuration.addProperty(SqlgGraph.DISTRIBUTED, true);
                if (!configuration.containsKey(SqlgGraph.JDBC_URL))
                    throw new IllegalArgumentException(String.format("SqlGraph configuration requires that the %s be set", SqlgGraph.JDBC_URL));
            }
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testAddEdgeUserSuppliedPK() {
        VertexLabel aVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().ensureVertexLabelExist(
                "A",
                new LinkedHashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                    put("name", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2"))
        );
        VertexLabel bVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().ensureVertexLabelExist(
                "B",
                new LinkedHashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                    put("name", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2"))
        );
        VertexLabel cVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().ensureVertexLabelExist(
                "C",
                new LinkedHashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                    put("name", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2"))
        );
        aVertexLabel.ensureEdgeLabelExist(
                "e1",
                bVertexLabel,
                new LinkedHashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2"))
        );
        aVertexLabel.ensureEdgeLabelExist(
                "e1",
                cVertexLabel,
                new LinkedHashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2"))
        );
        this.sqlgGraph.tx().commit();


        Vertex a1 = this.sqlgGraph.addVertex(T.label, "A", "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString(), "name", "a1");
        Vertex b1 = this.sqlgGraph.addVertex(T.label, "B", "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString(), "name", "b1");
        a1.addEdge("e1", b1, "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString());

        Vertex a2 = this.sqlgGraph.addVertex(T.label, "A", "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString(), "name", "a2");
        Vertex c2 = this.sqlgGraph.addVertex(T.label, "C", "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString(), "name", "c2");
        a2.addEdge("e1", c2, "uid1", UUID.randomUUID().toString(), "uid2", UUID.randomUUID().toString());

        this.sqlgGraph.tx().commit();


    }

    @Test
    public void testVertexCompositeIds() throws Exception {
        this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Person",
                new LinkedHashMap<String, PropertyType>() {{
                    put("name", PropertyType.varChar(100));
                    put("surname", PropertyType.varChar(100));
                    put("country", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("name", "surname"))
        );

        Optional<VertexLabel> personVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().getVertexLabel("Person");
        Assert.assertTrue(personVertexLabel.isPresent());
        Assert.assertEquals(2, personVertexLabel.get().getIdentifiers().size());
        Assert.assertEquals("name", personVertexLabel.get().getIdentifiers().get(0));
        Assert.assertEquals("surname", personVertexLabel.get().getIdentifiers().get(1));

        //HSQLDB does not support transactional schema creation.
        if (this.sqlgGraph.getSqlDialect().supportsTransactionalSchema()) {
            this.sqlgGraph.tx().rollback();
            personVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().getVertexLabel("Person");
            Assert.assertFalse(personVertexLabel.isPresent());
            if (this.sqlgGraph.getSqlDialect().supportsDistribution()) {
                personVertexLabel = this.sqlgGraph1.getTopology().getPublicSchema().getVertexLabel("Person");
                Assert.assertFalse(personVertexLabel.isPresent());
            }
        }

        this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Person",
                new LinkedHashMap<String, PropertyType>() {{
                    put("name", PropertyType.varChar(100));
                    put("surname", PropertyType.varChar(100));
                    put("country", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("name", "surname"))
        );
        this.sqlgGraph.tx().commit();
        Thread.sleep(1000);

        personVertexLabel = this.sqlgGraph.getTopology().getPublicSchema().getVertexLabel("Person");
        Assert.assertTrue(personVertexLabel.isPresent());
        Assert.assertEquals(2, personVertexLabel.get().getIdentifiers().size());
        Assert.assertEquals("name", personVertexLabel.get().getIdentifiers().get(0));
        Assert.assertEquals("surname", personVertexLabel.get().getIdentifiers().get(1));

        if (isPostgres()) {
            personVertexLabel = this.sqlgGraph1.getTopology().getPublicSchema().getVertexLabel("Person");
            Assert.assertTrue(personVertexLabel.isPresent());
            Assert.assertEquals(2, personVertexLabel.get().getIdentifiers().size());
            Assert.assertEquals("name", personVertexLabel.get().getIdentifiers().get(0));
            Assert.assertEquals("surname", personVertexLabel.get().getIdentifiers().get(1));
        }

        this.sqlgGraph.close();
        this.sqlgGraph = SqlgGraph.open(configuration);
        personVertexLabel = sqlgGraph.getTopology().getPublicSchema().getVertexLabel("Person");
        Assert.assertTrue(personVertexLabel.isPresent());
        Assert.assertEquals(2, personVertexLabel.get().getIdentifiers().size());
        Assert.assertEquals("name", personVertexLabel.get().getIdentifiers().get(0));
        Assert.assertEquals("surname", personVertexLabel.get().getIdentifiers().get(1));

        Thread.sleep(1000);
        dropSqlgSchema(sqlgGraph);
        this.sqlgGraph.tx().commit();
        this.sqlgGraph.close();
        try (SqlgGraph sqlgGraph = SqlgGraph.open(configuration)) {
            personVertexLabel = sqlgGraph.getTopology().getPublicSchema().getVertexLabel("Person");
            Assert.assertTrue(personVertexLabel.isPresent());
            Assert.assertEquals(2, personVertexLabel.get().getIdentifiers().size());
            Assert.assertEquals("name", personVertexLabel.get().getIdentifiers().get(0));
            Assert.assertEquals("surname", personVertexLabel.get().getIdentifiers().get(1));
        }
    }

    @Test
    public void testEdgeCompositeIds() throws Exception {
        VertexLabel personVertexLabel = this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Person",
                new LinkedHashMap<String, PropertyType>() {{
                    put("name", PropertyType.varChar(100));
                    put("surname", PropertyType.varChar(100));
                    put("country", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("name", "surname"))
        );
        VertexLabel addressVertexLabel = this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Address",
                new LinkedHashMap<String, PropertyType>() {{
                    put("street", PropertyType.varChar(100));
                    put("suburb", PropertyType.varChar(100));
                    put("country", PropertyType.STRING);
                    put("province", PropertyType.STRING);
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("street", "suburb"))
        );
        this.sqlgGraph.tx().commit();
        this.sqlgGraph.getTopology().ensureEdgeLabelExist(
                "livesAt",
                personVertexLabel,
                addressVertexLabel,
                new HashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2")));

        if (this.sqlgGraph.getSqlDialect().supportsTransactionalSchema()) {
            this.sqlgGraph.tx().rollback();
            Optional<EdgeLabel> livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
            Assert.assertFalse(livesAt.isPresent());
            if (this.sqlgGraph.getSqlDialect().supportsDistribution()) {
                livesAt = this.sqlgGraph1.getTopology().getEdgeLabel(this.sqlgGraph1.getSqlDialect().getPublicSchema(), "livesAt");
                Assert.assertFalse(livesAt.isPresent());
            }
        }

        this.sqlgGraph.getTopology().ensureEdgeLabelExist(
                "livesAt",
                personVertexLabel,
                addressVertexLabel,
                new HashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.varChar(100));
                    put("uid2", PropertyType.varChar(100));
                }},
                ListOrderedSet.listOrderedSet(Arrays.asList("uid1", "uid2")));
        this.sqlgGraph.tx().commit();

        Thread.sleep(1000);

        Optional<EdgeLabel> livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());
        Assert.assertEquals(2, livesAt.get().getIdentifiers().size());
        Assert.assertEquals("uid1", livesAt.get().getIdentifiers().get(0));
        Assert.assertEquals("uid2", livesAt.get().getIdentifiers().get(1));

        if (this.sqlgGraph.getSqlDialect().supportsDistribution()) {
            livesAt = this.sqlgGraph1.getTopology().getEdgeLabel(this.sqlgGraph1.getSqlDialect().getPublicSchema(), "livesAt");
            Assert.assertTrue(livesAt.isPresent());
            Assert.assertEquals(2, livesAt.get().getIdentifiers().size());
            Assert.assertEquals("uid1", livesAt.get().getIdentifiers().get(0));
            Assert.assertEquals("uid2", livesAt.get().getIdentifiers().get(1));
        }

        this.sqlgGraph.close();
        this.sqlgGraph = SqlgGraph.open(configuration);
        livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());
        Assert.assertEquals(2, livesAt.get().getIdentifiers().size());
        Assert.assertEquals("uid1", livesAt.get().getIdentifiers().get(0));
        Assert.assertEquals("uid2", livesAt.get().getIdentifiers().get(1));

        Thread.sleep(1000);

        dropSqlgSchema(this.sqlgGraph);
        this.sqlgGraph.tx().commit();
        this.sqlgGraph.close();

        this.sqlgGraph = SqlgGraph.open(configuration);
        livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());
        Assert.assertEquals(2, livesAt.get().getIdentifiers().size());
        Assert.assertEquals("uid1", livesAt.get().getIdentifiers().get(0));
        Assert.assertEquals("uid2", livesAt.get().getIdentifiers().get(1));
    }

    @Test
    public void testEdgeNormal() throws Exception {
        VertexLabel personVertexLabel = this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Person",
                new LinkedHashMap<String, PropertyType>() {{
                    put("name", PropertyType.STRING);
                    put("surname", PropertyType.STRING);
                    put("country", PropertyType.STRING);
                }});
        VertexLabel addressVertexLabel = this.sqlgGraph.getTopology().ensureVertexLabelExist(
                "Address",
                new LinkedHashMap<String, PropertyType>() {{
                    put("street", PropertyType.STRING);
                    put("suburb", PropertyType.STRING);
                    put("country", PropertyType.STRING);
                    put("province", PropertyType.STRING);
                }});
        this.sqlgGraph.tx().commit();
        this.sqlgGraph.getTopology().ensureEdgeLabelExist(
                "livesAt",
                personVertexLabel,
                addressVertexLabel,
                new HashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.STRING);
                    put("uid2", PropertyType.STRING);
                }});
        this.sqlgGraph.tx().rollback();

        if (this.sqlgGraph.getSqlDialect().supportsTransactionalSchema()) {
            Optional<EdgeLabel> livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
            Assert.assertFalse(livesAt.isPresent());
        }
        if (this.sqlgGraph.getSqlDialect().supportsDistribution()) {
            Optional<EdgeLabel> livesAt = this.sqlgGraph1.getTopology().getEdgeLabel(this.sqlgGraph1.getSqlDialect().getPublicSchema(), "livesAt");
            Assert.assertFalse(livesAt.isPresent());
        }

        this.sqlgGraph.getTopology().ensureEdgeLabelExist(
                "livesAt",
                personVertexLabel,
                addressVertexLabel,
                new HashMap<String, PropertyType>() {{
                    put("uid1", PropertyType.STRING);
                    put("uid2", PropertyType.STRING);
                }});
        this.sqlgGraph.tx().commit();

        Thread.sleep(1000);

        Optional<EdgeLabel> livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());

        if (this.sqlgGraph.getSqlDialect().supportsDistribution()) {
            livesAt = this.sqlgGraph1.getTopology().getEdgeLabel(this.sqlgGraph1.getSqlDialect().getPublicSchema(), "livesAt");
            Assert.assertTrue(livesAt.isPresent());
        }

        this.sqlgGraph.close();
        this.sqlgGraph = SqlgGraph.open(configuration);
        livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());

        Thread.sleep(1000);

        dropSqlgSchema(this.sqlgGraph);
        this.sqlgGraph.tx().commit();
        this.sqlgGraph.close();

        this.sqlgGraph = SqlgGraph.open(configuration);
        livesAt = this.sqlgGraph.getTopology().getEdgeLabel(this.sqlgGraph.getSqlDialect().getPublicSchema(), "livesAt");
        Assert.assertTrue(livesAt.isPresent());
    }
}
