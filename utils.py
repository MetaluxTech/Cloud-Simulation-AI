import os

def generate_tree(path, exclude):

    tree = ""
    for dirpath, dirnames, filenames in os.walk(path):
        depth = dirpath.replace(path, '').count(os.sep)
        dir_base_name = os.path.basename(dirpath)
        
        if dir_base_name in exclude:
            dirnames[:] = []  # remove directory names from list to prevent further walking
            continue

        indent = ' ' * 4 * (depth - 1)
        tree += '{}{}/\n'.format(indent, dir_base_name)
        for f in filenames:
            tree += '{}{}\n'.format(indent + ' ' * 4, f)
    with open('project_tree.txt', 'w') as f:
        f.write(tree)
    print("tree generated ")
    return tree

generate_tree('C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm', ['.venv', '.git','static','__pycache__'])
