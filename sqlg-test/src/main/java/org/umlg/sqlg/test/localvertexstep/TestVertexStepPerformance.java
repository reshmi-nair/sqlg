package org.umlg.sqlg.test.localvertexstep;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.DefaultGraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.sqlg.test.BaseTest;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/04/17
 * Time: 11:30 AM
 */
public class TestVertexStepPerformance extends BaseTest {

//    @Test
    public void test() {
        int count = 10_000;
        for (int i = 0; i < count; i++) {
            Vertex a1 = this.sqlgGraph.addVertex(T.label, "A");
            Vertex b1 = this.sqlgGraph.addVertex(T.label, "B");
            a1.addEdge("ab", b1);
        }
        this.sqlgGraph.tx().commit();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        DefaultGraphTraversal traversal = (DefaultGraphTraversal) this.sqlgGraph.traversal()
                .V().where(__.hasLabel("A"))
                .out();
        List<Vertex> vertices = traversal.toList();
        stopWatch.stop();
        System.out.println(stopWatch.toString());
        Assert.assertEquals(count, vertices.size());
    }

    @Test
    public void testGroup() {
        loadGratefulDead();

        DefaultGraphTraversal<Vertex, Map<String, Map<String, Number>>> traversal = (DefaultGraphTraversal<Vertex, Map<String, Map<String, Number>>>) this.sqlgGraph.traversal()
                .V()
                .out("followedBy")
                .<String, Map<String, Number>>group().by("songType").by(__.bothE().group().by(T.label).by(__.values("weight").sum()));

        printTraversalForm(traversal);
        final Map<String, Map<String, Number>> map = traversal.next();
        Assert.assertFalse(traversal.hasNext());
        Assert.assertEquals(3, map.size());
        Assert.assertTrue(map.containsKey(""));
        Assert.assertTrue(map.containsKey("original"));
        Assert.assertTrue(map.containsKey("cover"));
        //
        Map<String, Number> subMap = map.get("");
        Assert.assertEquals(1, subMap.size());
        Assert.assertEquals(179350, subMap.get("followedBy").intValue());
        //
        subMap = map.get("original");
        Assert.assertEquals(3, subMap.size());
        Assert.assertEquals(2185613, subMap.get("followedBy").intValue());
        Assert.assertEquals(0, subMap.get("writtenBy").intValue());
        Assert.assertEquals(0, subMap.get("sungBy").intValue());
        //
        subMap = map.get("cover");
        Assert.assertEquals(3, subMap.size());
        Assert.assertEquals(777982, subMap.get("followedBy").intValue());
        Assert.assertEquals(0, subMap.get("writtenBy").intValue());
        Assert.assertEquals(0, subMap.get("sungBy").intValue());
    }

}
