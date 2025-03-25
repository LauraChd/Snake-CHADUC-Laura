## Lancer une partie
=> Pour démarrer une partie, il faut exécuter le main de la classe SnakeApp.
- Pour changer de carte, il faut indiquer le nom de la nouvelle carte dans la création du ControllerSnakeGame
- Pour pouvoir jouer un des deux snakes au clavier, il faut mettre le paramètre mode à true dans la création du ControllerSnakeGame 
- Pour modifier la probabilité d'apparition d'un nouvel item lors qu'un snake en mange un, il faut modifier le paramètres prob dans la création du ControllerSnakeGame
- Pour changer le nombre de tour maximale, il faut modifier le paramètre maxTurn dans la création du ControllerSnakeGame
- IMPORTANT : Pour pouvoir contrôler le snake vert, il faut cliquer sur la fenêtre du jeu (cela donne le focus à la fenêtre du jeu et non à celle des commandes)

## Règles implémentées
- Une partie en solo est avec un snake jouable au clavier
- Une partie en duo est avec un snake jouable au clavier (vert) et un snake ordi (rouge)
- Un effet dure 20 tours
- Lorsque le snake est malade, il ne peut plus manger d'item
- Lorsque le snake est invincible, il peut traverser les murs, ne peut pas être mangé. Il peut manger tous les items mais seules les pommes lui font un effet

## Paramètres complémentaires
- A chaque tour, un item à une probabilité de 0.3 d'apparaître (cela permet d'avoir toujours des items qui apparaissent, même si le snake n'en mange pas dans le cas où il n'y en aurait pas)
- Il ne peut y avoir au plus que 3 items sur une carte et lorsqu'un nouveau apparaît, un autre disparaît
