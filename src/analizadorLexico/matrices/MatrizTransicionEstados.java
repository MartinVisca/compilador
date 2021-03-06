package analizadorLexico.matrices;

public class MatrizTransicionEstados {

    int[][] matriz = {    // Estado final = 12
            //		 l	d	.	_   l	f	<	>	=	!	+	-	*	/	%	\	n	;	,   '   (    )  {   }   o   Bl
            //		 0  1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17	18  19  20  21  22  23  24  25
            /*0*/	{11,1,	3, -1, 11, 11,	5,	5,	5,	5,  12,	12,	12, 8,	-1,	-1,	-1,	12,	12, 6,  12, 12, 12, 12, -1, 0},
            /*1*/	{-1,1,	3,	2, -1, -1, -1, -1, -1, -1,  -1,	-1,	-1, -1,	-1,	-1,	-1,	-1,	-1, -1, -1, -1, -1, -1, -1, -1},
            /*2*/	{12,-1,12, -1, 12, 12, 12, 12, 12, 12,	12,	12,	12, 12,	12,	12,	12, 12,	12, 12, 12, 12, 12, 12, 12, -1},
            /*3*/	{12,3, 12, 12, -1,	4, 12, 12, 12, 12,	12,	12,	12, 12,	12,	12,	12,	12,	12, 12, 12, 12, 12, 12, 12, 12},
            /*4*/	{12,4, 12, 12, -1, 12, 12, 12, 12, 12,  4,	4,	12, 12,	12,	12,	12, 12,	12, 12, 12, 12, 12, 12, 12, 12},
            /*5*/	{12,12,12, 12, -1, 12, 12, 12, 12, 12,  12,	12,	12, 12,	12,	12,	12,	12,	12, 12, 12, 12, 12, 12, 12, 12},
            /*6*/	{6,	6,	6,	6, -1,	6,	6,	6,	6,  6,	6,	6,	6,	6,	6,	7,	6,	6,	6,  12, 6,  6,  6,  6,  6,  6},
            /*7*/	{6,	6,	6,	6, -1,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	6,	12,	6,	6,  6,  6,  6,  6,  6,  6,  6},
            /*8*/	{12,12,12, 12, -1, 12, 12, 12, 12, 12,  12,	12,	12, 12,	9,  12,	12, 12,	12, 12, 12, 12, 12, 12, 12, 12},
            /*9*/	{9,	9,	9,	9, -1,	9,	9,	9,	9,  9,	9,	9,	9,  9,	10,	9,	9,	9,	9,  9,  9,  9,  9,  9,  9,  9},
            /*10*/	{9,	9,	9,	9, -1,	9,	9,	9,	9,	9,	9,	9,	9,	0,	10,	9,	9,	9,	9,  9,  9,  9,  9,  9,  9,  9},
            /*11*/	{11,11,12, 11, 11, 11, 12, 12, 12, 12,  12,	12,	12, 12,	12,	12,	12,	12,	12, 12, 12, 12, 12, 12, 12, 12},
    };

    public int get(int estadoOrigen, int simbolo) {
        return matriz[estadoOrigen][simbolo];
    }

}
