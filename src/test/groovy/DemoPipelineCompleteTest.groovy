import com.swedbank.jenkins.utilities.PipelineTestRunner
import spock.lang.Specification

class DemoPipelineCompleteTest extends Specification {
    PipelineTestRunner runner
    def methodCalls
    int index
    def scriptName = 'vars/demoPipeline.groovy'

    void setup() {
        runner = new PipelineTestRunner()
        runner.preferClassLoading = false
        methodCalls = [:]
        index = 0
    }

    def 'run through pipeline flow successfully'() {
        given:
        def currentResult = 'success'

        when:
        def demoPipeline = runner.load {
            script scriptName
            property 'any', { null }
            method 'echo', [String.class], { str ->
                methodCalls.put(++index + ' echo', 'My first step')
            }
            method 'sh', [String.class], { str ->
                methodCalls.put(++index + ' sh gradle', currentResult)
            }
            method 'customMethodOne', [String.class], { str ->
                methodCalls.put(++index + ' customMethodOne', currentResult)
            }
            method 'customMethodTwo', [String.class], { str ->
                methodCalls.put(++index + ' customMethodTwo', currentResult)
            }
        }
        demoPipeline()

        then:
        methodCalls.containsKey('1 echo')
        methodCalls['1 echo'] == 'My first step'
        methodCalls.containsKey('2 sh gradle')
        methodCalls['2 sh gradle'] == 'success'
        methodCalls.containsKey('3 customMethodOne')
        methodCalls.containsKey('4 customMethodTwo')
    }
}
