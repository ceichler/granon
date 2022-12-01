package utils;



	import agg.xt_basis.*;

	public class Grammar implements GraTraEventListener {



		/**
		 * The graph grammar
		 */
		public static final GraGra graphGrammar = BaseFactory.theFactory().createGraGra();

		/**
		 * Morphism completion strategy
		 */
		public static MorphCompletionStrategy strategy = CompletionStrategySelector.getDefault();


		/**
		 * Rules for the RDFS graph grammar, referenced for easier access
		 */
		public static Rule copyEdge, newNode, cloneSet, randomTransformSrc, randomTransformTarget, deleteNode, joinSet, modifyEdge, edgeCut, edgeReverse, edgeChord, edgeChordKeep;

		/**
		 * Where to load the grammar
		 */
		private final String fileLoadName;

		/**
		 * Initialize the class and gragra
		 */
		public Grammar() {
			this("anonOperator.ggx");
		}

		/**
		 * Initialize the class and gragra from some file
		 *
		 * @param fileName the file the gragra should be imported from
		 */
		public Grammar(String fileName) {
			this.fileLoadName = fileName;
			
			
			//import grammar
			importGragra();
			//Creating transformation utilities
			MorphCompletionStrategy strategy = CompletionStrategySelector.getDefault();
			strategy.removeProperty("dangling");
			strategy.removeProperty("injective");
			//strategy.enableParallelSearch(true);
			strategy.setProperty("NAC");
			strategy.setProperty("PAC");
			graphGrammar.setGraTraOptions(strategy);
			
		}



		public Graph getHostGraph() {
			return graphGrammar.getGraph();
		}

		public void setHostGraph(Graph graph) {
			graphGrammar.resetGraph();
			graphGrammar.importGraph(graph);
		}





		// UTILITY METHODS

		/**
		 * Imports and initialize the gragra from file fileLoadName
		 */
		private void importGragra() {

			Rule r;
			String rName;
			// importing Grammar
			try {
				// System.out.println("Importing grammar");
				graphGrammar.load(fileLoadName);
			} catch (Exception e) {
				System.out.println("Loading fail!");
				e.printStackTrace();
			}
			graphGrammar.setName("AnonOperator");

			//setting attributes "rules"
			for (int i = 0; i < graphGrammar.getListOfRules().size(); i++) {
				r = graphGrammar.getListOfRules().get(i);
				r.setEnabled(false);
				r.removeNAC(r.getNAC(0));
				rName = r.getName();
				switch (rName) {
					case "NewNode":
						Grammar.newNode = r;
						break;
					case "RandomTransformSource":
						Grammar.randomTransformSrc = r;
						break;
					case "RandomTransformTarget":
						Grammar.randomTransformTarget = r;
						break;
					case "JoinSet":
						Grammar.joinSet = r;
						break;
					case "DeleteNode":
						Grammar.deleteNode = r;
						break;

					case "ModifyEdge":
						Grammar.modifyEdge = r;
						break;
					case "EdgeCut":
						Grammar.edgeCut = r;
						break;
					case "EdgeReverse":
						Grammar.edgeReverse = r;
						break;
					case "EdgeChord":
						Grammar.edgeChord = r;
						break;
					case "EdgeChordKeep":
						Grammar.edgeChordKeep = r;
						break;
					case "EdgeCopy":
						Grammar.copyEdge = r;
						break;
					case "CloneSet":
						Grammar.cloneSet = r;
						break;
					default:
						System.out.println("Unknown rule while importing Graph Grammar: " + rName + " was the .ggx file modified?");
				}
			}
		}


		/**
		 * Implements GraTraEventListener
		 */
		public void graTraEventOccurred(GraTraEvent e) {
			String ruleName;
			switch (e.getMessage()) {

				case GraTraEvent.STEP_COMPLETED:
					ruleName = e.getMatch().getRule().getName();
					System.out.println(ruleName + " is applied");

					GraGraUtils.print(getHostGraph());

					System.out.println("Save transformed graphGrammar");
					String fn = graphGrammar.getName() + "_trans.ggx";
					graphGrammar.save(fn);
					System.out.println("Saved in " + fn);
					break;

				case GraTraEvent.NO_COMPLETION:
					ruleName = e.getMatch().getRule().getName();
					System.out.println(ruleName + " has no more completion");
					break;

				case GraTraEvent.MATCH_FAILED:
					ruleName = e.getRule().getName();
					System.out.println(ruleName + " has no more completion");
					break;

				case GraTraEvent.CANNOT_TRANSFORM:
					ruleName = e.getMatch().getRule().getName();
					System.out.println(ruleName + " cannot transform");
					break;

				case GraTraEvent.TRANSFORM_FINISHED:
					System.out.println("Transformation is finished");
					break;

				case GraTraEvent.INPUT_PARAMETER_NOT_SET:
				default:
					break;
			}
		}
	}


