from gaft import GAEngine
from gaft.components import BinaryIndividual,Population
from gaft.operators import RouletteWheelSelection, UniformCrossover, FlipBitMutation

indv_template = BinaryIndividual(ranges=[(0, 100)], eps=0.001)
population = Population(indv_template=indv_template, size=50).init()

selection = RouletteWheelSelection()
crossover = UniformCrossover(pc=0.8, pe=0.5)
mutation = FlipBitMutation(pm=0.1)

engine = GAEngine(population=population, selection=selection,
                  crossover=crossover, mutation=mutation,
                  analysis=[ConsoleOutput])
