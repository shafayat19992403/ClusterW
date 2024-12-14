import java.util.ArrayList;

public class UPGMA{
	
	//---------------------------------------
	// attributs
	//---------------------------------------
	
	private Noeud arbreGuide;
	private Sequences seqs;

	//---------------------------------------
	// constructeurs
	//---------------------------------------

	public UPGMA(Sequences seqs)
{
    this.seqs = seqs;
    int l_max = seqs.getListeSeq().size(); //nombre de sequences
    double[][] matDist = new double[l_max][l_max]; //matrice des distances permettant de construire l'arbre guide
    int i, j, l; //variables d'itération
    MatriceAlignSimple m; //matrice d'alignement qui sera utilisee pour aligner les paires de sequences entre elles
    MatriceScoreUnitaire scoreUnit = new MatriceScoreUnitaire(); //matrice de score unitaire
    double max; 
    int i_max, j_max; //le score max de la matrice de distance et ses coordonnees
    boolean bol = true;
    
    max = 0;
    i_max = 0;
    j_max = 0;
    l = l_max;

    // Création des feuilles (noeud) de l'arbre-guide
    ArrayList<Noeud> listeNoeuds = new ArrayList<Noeud>();
    
    for(i = 0; i < l; i++)
    {
        Noeud n = new Noeud(seqs.getSequence(i));
        listeNoeuds.add(n);
    }
    
    // Remplir la moitié supérieure de la matrice avec des scores d'alignement
    for(i = 0; i < l; i++)
    {
        for(j = i + 1; j < l; j++)
        {
            m = new MatriceAlignSimple(seqs.getSequence(i), seqs.getSequence(j), scoreUnit);
            matDist[i][j] = m.getScore();
        }
    }

    // Construire l'arbre et mettre à jour la matrice
    while(l > 1)
    {
        // Recherche du max de la matrice des distances
        max = 0;
        for(i = 0; i < l; i++)
        {
            for(j = i + 1; j < l; j++)
            {
                if(matDist[i][j] > max)
                {
                    max = matDist[i][j];
                    i_max = i;
                    j_max = j;
                }
            }
        }
        
        // Créer un noeud parent avec les séquences ayant le meilleur score comme fg et fd
        Noeud p = new Noeud(listeNoeuds.get(i_max), listeNoeuds.get(j_max));
        
        // Remplacer i_max par le nouveau parent
        listeNoeuds.set(i_max, p);
        
        // Supprimer j_max de la liste
        if (j_max > i_max) {
            listeNoeuds.remove(j_max);  // si j_max est plus grand que i_max, on peut le retirer directement
        } else {
            listeNoeuds.remove(j_max);  // sinon, j_max a été décalé d'un indice
        }
        
        // Mettre à jour la matrice de scores
        for(j = i_max + 1; j < l; j++)
        {
            matDist[i_max][j] = (matDist[i_max][j] + matDist[j_max][j]) / 2;
        }

        // Réduire l de 1
        l = l - 1;

        // Décaler la matrice de scores
        for(i = 0; i < l_max; i++)
        {
            for(j = j_max; j < l_max - 1; j++)
            {
                matDist[i][j] = matDist[i][j + 1];
            }
        }
        
        for(i = j_max; i < l_max - 1; i++)
        {
            for(j = i_max; j < l_max; j++)
            {
                matDist[i][j] = matDist[i + 1][j];
            }
        }

        // Remettre 0 dans les diagonales
        for(i = 0; i < l; i++)
        {
            matDist[i][i] = 0;
        }

        // Mettre à jour l'arbre guide
        this.arbreGuide = p;            
    }
}

	
	public void afficheMatDist(double[][]matDist, int l)
	{
		int i, j;
		
		for(i = 0; i < l; i++)
		{
			for(j = 0; j< i; j++)
			{
				System.out.print(" 0.0 ");
			}
			
			for(j = i; j < l; j++)
			{
				System.out.print(" " + matDist[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
		
	public Noeud getArbreGuide()
	{
		return arbreGuide;
	}
		
	public void afficheArbreGuide(Noeud n, int count) 
	{
		int i;
		String spacer = "-----";
			
		if(n != null)
		{
			count++;
			
			if(n.getCle() != null)
			{
				for(i = 0; i < count; i++)
				{
					System.out.print(spacer);
				}
				System.out.println(n.getCle().getName());
			}
			
			else
			{
				for(i = 0; i < count; i++)
				{
					System.out.print(spacer);
				}
				
				System.out.println("O");
			}
					
			if(n.getfg() != null)
			{
				afficheArbreGuide(n.getfg(), count);
			}
				
			if(n.getfd() != null)
			{
				afficheArbreGuide(n.getfd(), count);
			}
			
			count--;
		}
	} 
}
