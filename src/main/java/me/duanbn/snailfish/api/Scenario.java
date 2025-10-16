package me.duanbn.snailfish.api;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务场景.
 *
 * @author bingnan.dbn
 */
public class Scenario implements Serializable {

    private static final long serialVersionUID = 2499853582099716386L;

    private Map<String, Object> scenario = new ConcurrentHashMap<>();

    public Map<String, Object> getScenario() {
        return scenario;
    }

    public void addFeature(Map<String, Object> feature) {
        this.scenario.putAll(feature);
    }

    public void addFeature(String name, Object value) {
        this.scenario.putIfAbsent(name, value);
    }

    @SuppressWarnings("rawtypes")
    public Scenario addFeature(Enum scenarioFeature) {
        assert scenarioFeature != null : "feature should not be null";

        ScenarioFeature anno = scenarioFeature.getClass().getAnnotation(ScenarioFeature.class);
        if (anno == null) {
            throw new ScenarioException(
                    "scenario feature[" + scenarioFeature.getClass() + "] must be annotate by @ScenarioFeature");
        }

        String featureName = anno.value();

        this.scenario.put(featureName, scenarioFeature);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public static Scenario newInstance(Enum... features) {
        Scenario scenario = new Scenario();
        for (Enum feature : features) {
            scenario.addFeature(feature);
        }
        return scenario;
    }

    public String toString() {
        return "Scenario [scenario=" + scenario + "]";
    }

}
