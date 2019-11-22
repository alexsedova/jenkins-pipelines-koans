import com.swedbank.jenkins.utilities.PipelineTestRunner
import spock.lang.Specification

class DemoPipelineTest extends Specification {

    PipelineTestRunner runner

    def setup() {
        runner = new PipelineTestRunner()
        runner.preferClassLoading = false
    }

    def 'check if pipeline is loaded correctly'() {
        when:
        def stepScript = runner.load {
            script 'vars/demoPipeline.groovy'
            method ('customMethodOne', [String.class]) { null }
            method ('customMethodTwo', [String.class]) { null }
        }
        stepScript()
        then:
        assert stepScript.class.canonicalName == 'demoPipeline'
    }

    def 'check if sh command is called'() {
        given:
        List testSh = []
        when:
        def stepScript = runner.load {
            script 'vars/demoPipeline.groovy'
            method ('customMethodOne', [String.class]) { null }
            method ('customMethodTwo', [String.class]) { null }
            method ('sh', [String.class]) {
                str -> testSh.add(str)
            }
        }
        stepScript()
        then:
        assert testSh.size() == 1
        assert testSh.contains('gradle build')
    }

    def 'Fail the test stage'() {
        given:
        Object currentBuild = runner.binding.getVariable('currentBuild')
        List testEcho = []
        when:
        def stepScript = runner.load {
            script 'vars/demoPipeline.groovy'
            method ('customMethodOne', [String.class]) { str ->
                    if(str.contains('test')) {
                        currentBuild.result = 'FAILURE'
                    }
            }
            method ('echo', [String.class]) {
                str -> testEcho.add(str)
            }
            method ('customMethodTwo', [String.class]) {}
        }
        stepScript()
        then:
        assert testEcho.contains('Boo')
    }
}
