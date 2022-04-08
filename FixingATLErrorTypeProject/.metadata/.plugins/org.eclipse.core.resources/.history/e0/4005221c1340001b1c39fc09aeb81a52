//  IntSolutionType.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
// 
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.encodings.solutionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ca.udem.fixingatlerror.explorationphase.CoSolution;
import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.problems.AdaptiveInterface.Input;
//import jmetal.problems.AdaptiveInterface.CoSolution;
import jmetal.problems.AdaptiveInterface.Rule;
import jmetal.util.PseudoRandom;

/**
 * Class representing the solution type of solutions composed of Int variables
 */
public class IntSolutionType extends SolutionType {
	// MySolution S = new MySolution ();
	public static CoSolution S;
	public static ArrayList<Integer> operations; // = new ArrayList<Rule> ();
	// Input input = new Input ();
	public static int min_operations_size = 1;// 3 4 bood
	// public static int max_operations_size =22;
	public static int max_operations_size;// 8 bood

	public static int min_operations_interval = 1;
	// public static int max_operations_interval = 22 ;
	public static int max_operations_interval = 10;// 7 bod

	public static int operations_size;
	int h = 0;

	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public IntSolutionType(Problem problem) {
		super(problem);
	} // Constructor

	private boolean checksituationchh(int numoperation, boolean chh) {
		
		
		/*if (NSGAII.checkcollection == true) {

			if (NSGAII.checkfilter == false) {
				{
					if (NSGAII.checksequence == false) {
						if (NSGAII.checkoperationcall == true) {

							if (NSGAII.checkiteration == true) {
								if ((numoperation) != 4 && (numoperation) != 5) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}
							} else {
								if ((numoperation) != 4 && (numoperation) != 5 && (numoperation) != 10) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}

							}

						} else {
							if (NSGAII.checkiteration == true) {
								if ((numoperation) != 4 && (numoperation) != 5 && (numoperation) != 2) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}
							} else {
								if ((numoperation) != 4 && (numoperation) != 5 && (numoperation) != 2
										&& (numoperation) != 10) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}

							}
						}
					} else {
						if (NSGAII.checkoperationcall == true) {
							if (NSGAII.checkiteration == true) {
								if ((numoperation) != 5) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}
							} else {
								if ((numoperation) != 5 && (numoperation) != 10) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}
							}

						} else {
							if (NSGAII.checkiteration == true) {
								if ((numoperation) != 5 && (numoperation) != 2) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}
							} else {
								if ((numoperation) != 5 && (numoperation) != 2 && (numoperation) != 10) {
									operations.set(i, numoperation);
									check = true;
									temp.add(i);
									var++;
								}

							}

						}
					}

				}
			} else {
				if (NSGAII.checksequence == false) {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 4) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 4 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 2 && (numoperation) != 4) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 2 && (numoperation) != 4 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						}
					}
				} else {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							operations.set(i, numoperation);
							check = true;
							temp.add(i);
							var++;
						} else {
							if ((numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 2) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 10 && (numoperation) != 2) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}

					}
				}
			}
		} else if (NSGAII.checkcollection == false) {
			if (NSGAII.checkfilter == false) {
				if (NSGAII.checksequence == false) {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 5 && (numoperation) != 4 && (numoperation) != 7) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 5 && (numoperation) != 4 && (numoperation) != 7
									&& (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 5 && (numoperation) != 4 && (numoperation) != 7
									&& (numoperation) != 2) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 5 && (numoperation) != 4 && (numoperation) != 7
									&& (numoperation) != 2 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}

					}
				} else {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 5 && (numoperation) != 7) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 5 && (numoperation) != 7 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 5 && (numoperation) != 7 && (numoperation) != 2) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 5 && (numoperation) != 7 && (numoperation) != 2
									&& (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}

					}
				}
			} else {
				if (NSGAII.checksequence == false) {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 4 && (numoperation) != 7) {

								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 4 && (numoperation) != 7 && (numoperation) != 10) {

								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 4 && (numoperation) != 7 && (numoperation) != 2) {

								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 4 && (numoperation) != 7 && (numoperation) != 2
									&& (numoperation) != 10) {

								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}

						}

					}
				} else {
					if (NSGAII.checkoperationcall == true) {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 7) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 7 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						}
					} else {
						if (NSGAII.checkiteration == true) {
							if ((numoperation) != 2 && (numoperation) != 7) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						} else {
							if ((numoperation) != 2 && (numoperation) != 7 && (numoperation) != 10) {
								operations.set(i, numoperation);
								check = true;
								temp.add(i);
								var++;
							}
						}

					}
				}
			}

		}*/
		return chh;
	}

	/**
	 * Creates the variables of the solution
	 */
	public Variable[] createVariables() {

		// System.out.println("v222");
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		int lastoperationssize = max_operations_size;

		operations = new ArrayList<Integer>();
		Random number_generator = new Random();
		operations_size = min_operations_size
				+ (int) (Math.random() * ((max_operations_size - min_operations_size) + 1));
		// operations_size = number_generator.nextInt(max_operations_size);
		// if (operations_size < min_operations_size) operations_size =
		// min_operations_size;
		
		// for (int var = 0; var < problem_.getNumberOfVariables(); var++)
		h++;
		// System.out.println("********** Solution X************ " +h );
		int numoperation = -1;
		int var = 0;
		// 6 bod be tedade array n error
		

		for (int i = 0; i < lastoperationssize; i++)
			operations.add(-3);
		// int temp=-1;
		List<Integer> temp = new ArrayList<Integer>();
		int i = -1;
		// for (int var = 0; var <operations_size; var++)
		
	//	System.out.println("Forbidden Operations:" + NSGAII.forbiddenoperations);
		while (var < operations_size) // var<2
		{
			boolean tp = false;
			while (tp == false) {
				i = number_generator.nextInt(lastoperationssize);
				if (!temp.contains(i))
					tp = true;
			}
			boolean check = false;
			variables[var] = new Int((int) problem_.getLowerLimit(var), (int) problem_.getUpperLimit(var));
			// while(mut==false) {
			numoperation = min_operations_interval
					+ number_generator.nextInt(max_operations_interval - min_operations_interval + 1);
			
				if(!NSGAII.forbiddenoperations.contains(numoperation))
			{
				operations.set(i, numoperation);
				check = true;
				temp.add(i);
				var++;
			}
			
			
			
		}
		System.out.println("le num aleatoire de l operation est : " + operations);
		return variables;
	}

	
}
