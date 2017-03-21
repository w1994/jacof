package thiagodnf.jacof.aco.graph.initialization;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.mscharhag.oleaster.runner.OleasterRunner;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.problem.Problem;

@RunWith(OleasterRunner.class)
public class MMASInitializationTest {{
	
	Problem problem = Mockito.mock(Problem.class);
	ACO aco = Mockito.mock(ACO.class);
	
	when(aco.getNumberOfAnts()).thenReturn(2);
	when(aco.getProblem()).thenReturn(problem);
	when(aco.getProblem().getCnn()).thenReturn(10.0);
	when(aco.getProblem().getNumberOfNodes()).thenReturn(5);
	
	describe("When initialize an EASInitialization", () -> {

		it("should throw an exception when null aco is passed", () -> {
			expect(() -> {
				new MMASInitialization(null);
			}).toThrow(NullPointerException.class);
		});
		
		it("should throw an exception when rate > 1", () -> {
			expect(() -> {
				new MMASInitialization(aco, 1.01);
			}).toThrow(IllegalArgumentException.class);
		});
		
		it("should throw an exception when rate < 0", () -> {
			expect(() -> {
				new MMASInitialization(aco, -0.01);
			}).toThrow(IllegalArgumentException.class);
		});
	});
	
	describe("When rate=0.02 and Cnn=10", () -> {
		
		it("should return 5.0", () -> {
			expect(new MMASInitialization(aco).getT0()).toEqual(5.0);
		});
	});
	
	describe("When call toString method", () -> {

		it("should return the correct name", () -> {
			expect(new MMASInitialization(aco, 0.5).toString()).toEqual("MMASInitialization 0.5");
		});
	});
}}
