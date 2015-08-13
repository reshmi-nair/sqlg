package org.umlg.sqlg.test.gremlincompile;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.PartitionStrategy;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.sqlg.predicate.Text;
import org.umlg.sqlg.structure.SqlgProperty;
import org.umlg.sqlg.test.BaseTest;
import org.umlg.sqlg.test.SqlGProvider;

import java.util.Collections;
import java.util.List;

/**
 * Created by pieter on 2015/08/03.
 */
public class TestGremlinCompileWhere extends BaseTest {

//    @Test
//    public void testEquals() {
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "johnny");
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "pietie");
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "koosie");
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", P.eq("johnny")).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", P.eq("johnnyxxx")).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testNotEquals() {
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "johnny");
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "pietie");
//        this.sqlgGraph.addVertex(T.label, "Person", "name", "koosie");
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", P.neq("johnny")).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", P.neq("johnnyxxx")).toList();
//        Assert.assertEquals(3, vertices.size());
//    }
//
//    @Test
//    public void testBiggerThan() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gt(0)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gt(1)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gt(2)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gt(3)).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testBiggerEqualsTo() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gte(0)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gte(1)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gte(2)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gte(3)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.gte(4)).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testSmallerThan() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lt(0)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lt(1)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lt(2)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lt(3)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lt(4)).toList();
//        Assert.assertEquals(3, vertices.size());
//    }
//
//    @Test
//    public void testLessThanEqualsTo() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lte(0)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lte(1)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lte(2)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lte(3)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.lte(4)).toList();
//        Assert.assertEquals(3, vertices.size());
//    }
//
//    //Note gremlin between is >= and <
//    @Test
//    public void testBetween() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.between(0, 4)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.between(1, 4)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.between(1, 3)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.between(1, 1)).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testInside() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.inside(0, 4)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.inside(1, 4)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.inside(1, 3)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.inside(1, 1)).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testOutside() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.outside(0, 4)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.outside(1, 4)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.outside(1, 3)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.outside(1, 1)).toList();
//        Assert.assertEquals(2, vertices.size());
//    }
//
//    @Test
//    public void testWithin() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(1, 2, 3)).toList();
//        Assert.assertEquals(3, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(0, 1)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(1, 3)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(1, 1)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(3, 4)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(4, 5)).toList();
//        Assert.assertEquals(0, vertices.size());
//    }
//
//    @Test
//    public void testWithout() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(1, 2, 3)).toList();
//        Assert.assertEquals(0, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(0, 1)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(1, 3)).toList();
//        Assert.assertEquals(1, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(1, 1)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(3, 4)).toList();
//        Assert.assertEquals(2, vertices.size());
//        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.without(4, 5)).toList();
//        Assert.assertEquals(3, vertices.size());
//    }
//
//    @Test
//    public void testEmptyWithin() {
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 1);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 2);
//        this.sqlgGraph.addVertex(T.label, "Person", "age", 3);
//        this.sqlgGraph.tx().commit();
//        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("age", P.within(Collections.emptyList())).toList();
//        Assert.assertEquals(0, vertices.size());
//    }

    @Test
    public void testTextEq() {
        this.sqlgGraph.addVertex(T.label, "Person", "name", "aaaaa");
        this.sqlgGraph.addVertex(T.label, "Person", "name", "abcd");
        this.sqlgGraph.addVertex(T.label, "Person", "name", "john");
        this.sqlgGraph.tx().commit();
        List<Vertex> vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", Text.eq("a")).toList();
        Assert.assertEquals(2, vertices.size());
        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", Text.eq("aaa")).toList();
        Assert.assertEquals(1, vertices.size());
        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", Text.eq("abc")).toList();
        Assert.assertEquals(1, vertices.size());
        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", Text.eq("acd")).toList();
        Assert.assertEquals(0, vertices.size());
        vertices = this.sqlgGraph.traversal().V().hasLabel("Person").has("name", Text.eq("ohn")).toList();
        Assert.assertEquals(1, vertices.size());
    }

}
