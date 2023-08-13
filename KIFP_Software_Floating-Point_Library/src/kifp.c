/* Only Edit This File
 * ------------------
 *  Name: Akram Beshir
 *  GNumber: G01128864
 */

#include <stdio.h>
#include <stdlib.h>
#include "common_structs.h"
#include "common_functions.h"
#include "kifp.h"

// Feel free to add many Helper Functions, Consts, and Definitions!

#define BIAS 3 //BIAS FOR AN EXP FIELD WITH 3 BITS
#define EDEN -2 //BIG FOR A DENORMALIZED VALUE

int isDem(kifp_t j){
	int w = 0;
	int x = (((1 << 3) - 1) & (j >> 5)); //EXP OF THE kifp
	if(x==0){ //exp of 0 == denormalized
		w = 0;
	}
	else{
		w = 1;
	}
	return w;
}

int bigE(kifp_t a, int b){//TAKES PARAMETERS OF A KIFP AND AN INT FROM THE PREV. HELPFER FUNCTION THAT DETERMINES IF IT'S DENORMALIZED
	int ans = 0;
	int x = (((1 << 3) - 1) & (a >> 5));
	if(b==0){
		ans = EDEN;
	}
	else{
		ans = x - BIAS;
	}
	return ans;
}

int specMul(kifp_t o, kifp_t t){//HELPFER FUNCTION TO DEAL W/ SPECIAL CASES FOR MULTIPLICATION
       int exo = (((1 << 3) - 1) & (o >> 5));//exp of first value
       int ext = (((1 << 3) - 1) & (t >> 5));//exp of second value
       int fraco = (((1 << 5) - 1) & (o >> 0));//frac of 1st value
       int fract = (((1 << 5) - 1) & (t >> 0));//frac of 2nd value
       int notnum = 0;
       if((exo==7 && fraco!=0) || (ext==7 && fract!=0)){//if one of them is NAN
	       int notnum = 1; //NAN
	       return notnum;
       }
       else if((exo==7 && fraco==0) && (t==0)){//INF * 0
	       int notnum = 1;//NAN
	       return notnum;
       }
       else if((ext==7 && fract==0) && (o==0)){//0 * INF
	       int notnum = 1;//NAN
	       return notnum;
       }
       else if((exo==7 && fraco==0) && (t!=0)){//INF * X
	       int notnum = 2;//INF
	       return notnum;
       }
       else if((ext==7 && fract==0) && (o!=0)){//X * INF
	       int notnum = 2;//INF
	       return notnum;
       }
       else if(o==0||t==0){//0 * X || X * 0
	       int notnum = 3;//0
	       return notnum;
       }
       else if(o==256||t==256){//-0 * X
	       int notnum = 4;//-0
	       return notnum;
       }
       return notnum;
}

int specAdd(kifp_t o, kifp_t t){
       int s1 = !!(o & (1<<8));//SINGED BITS OF THE VALUES
       int s2 = !!(t & (1<<8));
       int exo = (((1 << 3) - 1) & (o >> 5));//EXP FIELDS
       int ext = (((1 << 3) - 1) & (t >> 5));
       int fraco = (((1 << 5) - 1) & (o >> 0));//FRAC FIELDS
       int fract = (((1 << 5) - 1) & (t >> 0));
       int notnum = 0;
       if((exo==7 && fraco!=0) || (ext==7 && fract!=0)){//IF ONE OF THEM IS NAN
	       int notnum = 1; //NAN
	       return notnum;
       }
       else if((exo==7 && fraco==0) && (ext==7 && fract==0)){
	       if((s1==0 && s2==1) || (s1==1 && s2==0)){//-INF + INF
		       int notnum = 1;//NAN
		       return notnum;
	       }
	       else if(s1==0 && s2==0){//INF + INF
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1 && s2==1){//-INF + -INF
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(exo==7 && fraco==0 && t==0){//INF + 0
	       if(s1==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1){//-INF + 0
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(exo==7 && fraco==0){//INF + X
	       if(s1==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1){//-INF + X
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(ext==7 && fract==0 && o==0){
	       if(s2==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s2==1){
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(ext==7 && fract==0){
	       if(s2==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s2==1){
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(((s1==0 && s2==1) || (s1==1 && s2==0)) && ((((1 << 8) - 1) & (o >> 0)) == (((1 << 8) - 1) & (t >> 0)))){
	       int notnum = 4; //0
	       return notnum;
       }
       else if((o==0 && (t==0 || t==256)) || (t==0 && (o==0 || o==256))){ //256 IS 0 with the Signed bit as 1
	       int notnum = 4; //0
	       return notnum;
       }
       else if(o==256 && t==256){
	       int notnum = 5; //-0
	       return notnum;
       }
       else if((o==0||o==256) && (t!=0||t!=256||t!=480)){//480 is INF WITH THE SINGED BIT AS 1
	       int notnum = 6;
	       return notnum; //t aka kifp val2
       }
       else if((t==0||t==256) && (o!=0||o!=256||o!=480)){
	       int notnum = 7; //o aka kifp val1
	       return notnum;
       }
       return notnum;
}

int specSub(kifp_t o, kifp_t t){
       int s1 = !!(o & (1<<8));
       int s2 = !!(t & (1<<8));
       int exo = (((1 << 3) - 1) & (o >> 5));
       int ext = (((1 << 3) - 1) & (t >> 5));
       int fraco = (((1 << 5) - 1) & (o >> 0));
       int fract = (((1 << 5) - 1) & (t >> 0));
       int notnum = 0;
       if((exo==7 && fraco!=0) || (ext==7 && fract!=0)){
	       int notnum = 1; //NAN
	       return notnum;
       }
       else if((exo==7 && fraco==0) && (ext==7 && fract==0)){
	       if(s1==s2){
		       int notnum = 1;//NAN
		       return notnum;
	       }
	       else if(s1==0 && s2==1){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1 && s2==0){
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(exo==7 && fraco==0 && t==0){
	       if(s1==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1){
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(exo==7 && fraco==0){
	       if(s1==0){
		       int notnum = 2;//+INF
		       return notnum;
	       }
	       else if(s1==1){
		       int notnum = 3;//-INF
		       return notnum;
	       }
       }
       else if(ext==7 && fract==0 && o==0){
	       if(s2==0){
		       int notnum = 3;//-INF
		       return notnum;
	       }
	       else if(s2==1){
		       int notnum = 2;//+INF
		       return notnum;
	       }
       }
       else if(ext==7 && fract==0){
	       if(s2==0){
		       int notnum = 3;//-INF
		       return notnum;
	       }
	       else if(s2==1){
		       int notnum = 2;//+INF
		       return notnum;
	       }
       }
       else if((s1==s2) && ((((1 << 8) - 1) & (o >> 0)) == (((1 << 8) - 1) & (t >> 0)))){
	       int notnum = 4; //0
	       return notnum;
       }
       else if(o==0 && t==256){
	       int notnum = 4; //0
	       return notnum;
       }
       else if(o==256 && t==0){
	       int notnum = 5; //-0
	       return notnum;
       }
       else if((o==0||o==256) && (t!=0||t!=256||t!=480)){
	       int notnum = 6;
	       return notnum; //t aka kifp val2
       }
       else if((t==0||t==256) && (o!=0||o!=256||o!=480)){
	       int notnum = 7; //o aka kifp val1
	       return notnum;
       }
       return notnum;
}

// ----------Public API Functions (write these!)-------------------

// toKifp - Converts a Number Struct (whole and fraction parts) into a KIFP Value
// number is managed by zuon, DO NOT FREE number.
// Return the KIFP Value on Success or -1 on Failure.
kifp_t toKifp(Number_t *number) {
	int b = 0;
	int expp = 0; //EXP
	int ex = 0; //EXP AFTER PUTTING THEM IN BITS 5-7
	int frac = number->fraction;
	int fr = 0; //FRAC AFTER PUTTING THEM IN BITS 0-4
	int whol = number->whole;
	int e = 0;
	int p = 0;
	int i = 0xE0; // 0  1 1 1  0 0 0 0 0
	int n = 0xE1; // 0  1 1 1  0 0 0 0 1
	if(number->is_nan==0 && number->is_infinity==0 && number->whole==0 && number->fraction==0){//POSSIBLE 0
		if(number->is_negative==1){
			return 256;
		}
		else{
			return 0;
		}
	}
	if(number->is_nan==1 && number->is_infinity==1){
		return n;//NAN IF BOTH IS INF AND IS NAN FLAGS ARE 1
	}
	else if(number->is_infinity==1){
		if(number->is_negative==1){
			i = (i | (1 << 8));//SET THE SIGNED BIT IN ACCORDANCE TO THE IS NEGATIVE FLAG
		}
		else if(number->is_negative==0){
			i = (i & (~(1 << 8)));
		}
		return i;
	}
	else if(number->is_nan==1){
		return n;
	}
	if(whol>1){
		while(whol!=1){//UNTIL WE GET OUT MANTISSA TO BE 1
			if(!!(whol & (1<<31)) == 0){ //IF THE MSB OF WHOLE IS 0
				b = !!(whol & (1<<0)); //GET THE CURRENT LSB OF WHOLE
				whol = whol>>1; //SIMPLY RIGHT SHIFT WHOLE BY 1 BIT
				frac = frac>>1; //RIGHT SHIFT FRACTION BY 1 BIT
				if(b==0){ //SET THE MSB OF FRACTION TO BE THE PREVIOUS LSB OF WHOLE
					frac = (frac & (~(1 << 31)));
				}
				else{
					frac = (frac | (1 << 31));
				}
			}
			else{ //IF THE MSB OF WHOLE IS 1
				b = !!(whol & (1<<0));
				whol = whol>>1;//SIMPLY RIGHT SHIFT WHOLE BY 1 BIT
				whol = (whol & (~(1 << 31)));//SET THE MSB OF WHOLE TO BE 0 SINCE AN MSB OF 1 WILL FILL BIT 31 WITH 1 WHEN SHIFTING RIGHT
				frac = frac>>1;//RIGHT SHIFT FRACTION BY 1 BIT
				if(b==0){ //SET THE MSB OF FRACTION TO BE THE PREVIOUS LSB OF WHOLE
					frac = (frac & (~(1 << 31)));
				}
				else{
					frac = (frac | (1 << 31));
				}
			}
			e++; //INCREMENT THE BIG E
		}
	}
	else if(whol==0){
		while(!!(frac & (1<<31))!=1){ //WHILE THE MSB OF FRACTION IS NOT 1
			frac = frac << 1; //KEEP SHIFTING TO THE LEFT
			e--; //DECREMENT THE BIG E
		}
		whol = (whol | (1 << 0)); //SET THE LSB OF WHOL (0) TO BE 1
		frac = (frac & (~(1 << 31)));//SET THE MSB OF FRAC (1) TO 0. U MIGHT NOT NEED TO DO THIS BC LEFT SHIFT OF SIGNED INT WILL GET RID OF MSB
		frac = frac << 1; //SHIFT THE FRAC TO THE LEFT BY ONE BIT
		e--; //DECREMENT THE BIG E
	}
	expp = e + BIAS;
	if(expp<=0){ //DENORMALIZED
		while(e != EDEN){
			b = !!(whol & (1<<0));
			whol = whol>>1; //KEEP SHIFTING WHOL AND FRAC TO THE RIGHT
			frac = frac>>1;
			if(b==0){
				frac = (frac & (~(1 << 31)));
			}
			else{
				frac = (frac | (1 << 31));
			}
			e++; //INCREMENT E
		}
		fr = (((1 << 5) - 1) & (frac >> 27));
		p = fr;
		if(number->is_negative==1){
			p = (p | (1 << 8));
		}
		else if(number->is_negative==0){
			p = (p & (~(1 << 8)));
		}
		return p;
	}
	else if(expp>=7){ //INF
		if(number->is_negative==1){
			i = (i | (1 << 8));
		}
		else if(number->is_negative==0){
			i = (i & (~(1 << 8)));
		}
		return i;
	}
	else{ //NORMALIZED
		ex = expp << 5; //GET EXP TO BE IN BITS 5-7
		fr = (((1 << 5) - 1) & (frac >> 27));
		p = ex | fr;
		if(number->is_negative==1){
			p = (p | (1 << 8));
		}
		else if(number->is_negative==0){
			p = (p & (~(1 << 8)));
		}
		return p;
	}
	return -1; //ASK TA WHAT TYPE OF ERROR YOU COULD GET
}

// toNumber - Converts a KIFP Value into a Number Struct (whole and fraction parts)
// number is managed by zuon, DO NOT FREE or re-Allocate number. (It is already allocated)
// Return 0 on Success or -1 on Failure.
int toNumber(Number_t *number, kifp_t value) {
	int whol = 1;
	int frac = 0;
	int expp = 0;
	int e = 0;
	int b = 0;
	if(!!(value & (1<<8))==1){ //CHECKS THE SIGNED BIT
		number->is_negative = 1;
	}
	else{
		number->is_negative = 0;
	}
	expp = (((1 << 3) - 1) & (value >> 5));
	if(expp==7){//INF
		if((((1 << 5) - 1) & (value >> 0)) == 0){
			number->is_infinity = 1;
			number->is_nan = 0;
			number->fraction = 0x0; //DOUBLE CHECK WITH TA TO C IF WHOLE AND FRAC CAN BE ANYTHING FOR INF AND NAN
			number->whole = 0x1;
		}
		else{
			number->is_infinity = 0;
			number->is_nan = 1;
			number->fraction = 0x1;
			number->whole = 0x1;
		}
		return 0;
	}
	else if(expp==0){//DENORMALIZED
		e = EDEN;
		frac = (((1 << 5) - 1) & (value >> 0));
		frac = frac << 27;
		while(e<expp){
			b = !!(frac & (1<<31));
			whol = 0;
			if(b==1){
				frac = frac >> 1;
				frac = (frac & (~(1 << 31)));
			}
			else{
				frac = frac >> 1;
			}
			e++;
		}
		number->is_infinity = 0;
		number->is_nan = 0;
		number->whole = whol;
		number->fraction = frac;
		return 0;
	}
	frac = (((1 << 5) - 1) & (value >> 0));
	frac = frac << 27; //GET THE FRAC FIELD TO FILL UP THE THE 5 MOST SIGNIFICANT BITS FOR THE FRACTION MEMBER IN OUR NUMBER STRUCT
	e = expp - BIAS;//normalized
	if(e==0){
		number->whole = 1;
		number->fraction = frac;
		number->is_nan = 0;
		number->is_infinity = 0;
		return 0;
	}
	else if(e>0){
		while(e>0){ //SHIFT LEFT TO GET OUR MANTISSA IN THE RIGHT RANGE
			b = !!(frac & (1<<31));
			whol = whol << 1;
			frac = frac << 1;
			if(b==1){ //KEEPS TRACK OF FRACS MSB AND INSERTS IT INTO WHOLE
				whol = (whol | (1 << 0));
			}
			else{
				whol = (whol & (~(1 << 0)));
			}
			e--;
		}
		number->whole = whol;
		number->fraction = frac;
		number->is_nan = 0;
		number->is_infinity = 0;
		return 0;
	}
	else if(e<0){
		while(e<0){ //SHIFT RIGHT TO GET OUR MANTISSA IN THE RIGHT RANGE
			if(!!(whol & (1<<31)) == 0){
				b = !!(whol & (1<<0));
				whol = whol>>1;
				frac = frac>>1;
				if(b==0){
					frac = (frac & (~(1 << 31)));
				}
				else{
					frac = (frac | (1 << 31));
				}
			}
			else{
				b = !!(whol & (1<<0));
				whol = whol>>1;
				whol = (whol & (~(1 << 31)));
				frac = frac>>1;
				if(b==0){
					frac = (frac & (~(1 << 31)));
				}
				else{
					frac = (frac | (1 << 31));
				}
			}
			e++;
		}
		number->whole = whol;
		number->fraction = frac;
		number->fraction = frac;
		number->is_nan = 0;
		number->is_infinity = 0;
		return 0;
        }
	return -1;
}

// mulKIFP - Multiplies two KIFP Values together using the Techniques from Class.
// - To get credit, you must work with S, M, and E components.
// - You are allowed to shift/adjust M and E to multiply whole numbers.
// Return the resulting KIFP Value on Success or -1 on Failure.
kifp_t mulKifp(kifp_t val1, kifp_t val2) {
	int s = !!(val1 & (1<<8)) ^ !!(val2 & (1<<8)); //XOR THE SIGNED BITS TO C WHAT OUR SIGNED BIT SHOULD BE FOR OUR PRODUCT
	int fin = specMul(val1, val2); //USES THE HELPFER TO LOOK FOR SPECIAL CASES FIRST B4 MOVING ON
	int infin = 0x0E0;//INF
	int notn = 0x0E1;//NAN
	if(fin==1){
		return notn;
	}
	else if(fin==2){
		if(s==1){
			infin = (infin | (1 << 8));
		}
		else if(s==0){
			infin = (infin & (~(1 << 8)));
		}
		return infin;
	}
	else if(fin==3){
		if(s==1){
			infin = (0 | (1 << 8));
		}
		else if(s==0){
			infin = (0 & (~(1 << 8)));
		}
		return infin;
	}
	else if(fin==4){
		if(s==1){
			infin = (256 | (1 << 8));
		}
		else if(s==0){
			infin = (256 & (~(1 << 8)));
		}
		return infin;
	}
	int whol1 = isDem(val1);//USES HELPER FUNC TO C IF WE HAVE DENORMALIZED VALUES AND SET THE WHOLES ACCORDINGLY
	int whol2 = isDem(val2);
	int e1 = bigE(val1, whol1);//USES HELPFER FUNCTION TO SET THE BIG E's OF OUR VALUES
	int e2 = bigE(val2, whol2);
	int e = e1 + e2;
	int frac1 = (((1 << 5) - 1) & (val1 >> 0));//GETS FRAC FIELDS
	int frac2 = (((1 << 5) - 1) & (val2 >> 0));
	whol1 = whol1 << 5;//TO MULTIPLY EVERYTHING AS A WHOL, SHIFT TO LEFT 5 BITS TO MAKE SPACE FOR THE FRAC FIELDS
        whol1 = whol1 | frac1;
        whol2 = whol2 << 5;
        whol2 = whol2 | frac2;
        int m = whol1 * whol2;//MULTIPLY THE WHOLES
	e-=10;//SHIFT LEFT 5 BITS FOR BOTH VALUES TO GET A WHOLE; -5 + -5 = -10
        m = m >> 5;//SHIFT BACK NOW
	e+=5;
        int frac = (((1 << 5) - 1) & (m >> 0));//FRAC FIELD OF PRODUCT
        m = m >> 5;
	e+=5;
        if(m>1){ //SHIFT RIGHT TO GET MANTISSA IN RIGHT RANGE
		while(m!=1){
			int c = !!(m & (1<<0));
			m = m >> 1;
			frac = frac >> 1;
			if(c==1){//KEEPS TRACK OF WHOLS LSB WHEN SHIFTING TO PUT IN FRAC
				frac = (frac | (1 << 4));
			}
			else if(c==0){
				frac = (frac & (~(1 << 4)));
			}
			e++;
		}
	}
	else if(m<1){ //SHIFT LEFT TO GET MANTISSA IN RIGHT RANGE
		while(m!=1){
			int l = !!(frac & (1<<4));
			m = m << 1;
			frac = frac << 1;
			if(l==1){
				m = (m | (1 << 0));
			}
			else{
				m = (m & (~(1 << 0)));
			}
			e--;
		}
	}
	int ex = e + BIAS;
	int ans = 0;
	int z = 0;
	if(ex<=0){//DENORMALIZED
		while(e!=EDEN){//SHIFT UNTIL E = -2
                        z = !!(m & (1<<0));
                        m = m >> 1;
                        frac = frac >> 1;
                        if(z==1){
                                frac = (frac | (1 << 4));
                        }
                        else if(z==0){
                                frac = (frac & (~(1 << 4)));
                        }
                        frac = (((1 << 5) - 1) & (frac >> 0));
                        e++;
                }
                ex = 0;
	}
	else if(ex>=7){//INF
		ex = 7;
		ex = ex << 5;
		frac = 0;
	}
	else{//NORMALIZED
		ex = ex << 5; //GET EXP TO BE IN BITS 5-7
	}
	ans = ex | frac; 
        if(s==1){ //IF SIGNED BIT IS 1
		ans = (ans | (1 << 8));
		return ans;
	}
	else{
		ans = (ans & (~(1 << 8)));
                return ans;
	}
	return -1;
}

// addKIFP - Adds two KIFP Values together using the Addition Techniques from Class.
// - To get credit, you must work with S, M, and E components.
// - You are allowed to shift/adjust M and E as needed.
// Return the resulting KIFP Value on Success or -1 on Failure.
kifp_t addKifp(kifp_t val1, kifp_t val2) {
	int fin = specAdd(val1, val2);//HELPER FUNCTION FOR ADDITION SPECIAL CASES
	int infin = 0x0E0;
	int notn = 0x0E1;
	if(fin==1){
		return notn;
	}
	else if(fin==2){
		return infin;
	}
	else if(fin==3){
		infin = infin | 256;
		return infin;
	}
	else if(fin==4){
		infin = 0;
		return infin;
	}
	else if(fin==5){
		infin = 256;
		return infin;
	}
	else if(fin==6){
		return val2;
	}
	else if(fin==7){
		return val1;
	}
	int e1 = 0;
	int e2 = 0;
	int whol1 = 0;
	int whol2 = 0;
	int ex1 = (((1 << 3) - 1) & (val1 >> 5));//GETS EXP FIELDS FROM THE VALUES
	int ex2 = (((1 << 3) - 1) & (val2 >> 5));
	int n = 0;
	int e = 0;
	int z = 0;
	if(ex1==0){//DENORMALIZED
		whol1 = 0;
		e1 = EDEN;
	}
	else{
		whol1 = 1;
		e1 = ex1 - BIAS;
	}
	if(ex2==0){//DENORMALIZED
		whol2 = 0;
		e2 = EDEN;
	}
	else{
		whol2 = 1;
		e2 = ex2 - BIAS;
	}
	int en = 0;
	int frac1 = (((1 << 5) - 1) & (val1 >> 0));//GETS FRAC FIELD FROM THE VALUES
	int frac2 = (((1 << 5) - 1) & (val2 >> 0));
	whol1 = whol1 << 5; //SHIFT THE VALUES SO YOU CAN ADD THEM AS WHOLE #'S
	e1-=5;
	whol1 = whol1 | frac1;
	whol2 = whol2 << 5;
	e2-=5;
	whol2 = whol2 | frac2;
	e = e1;
	if(e1>e2){//E'S NEED TO BE THE SAME TO PEFROM ADDING AND SUBTRACTING
		en = e2;
		while(e1!=e2){
			whol1 = whol1 << 1;
			e1--;
		}
		e = e2;
	}
	else if(e2>e1){
		en = e1;
		while(e2!=e1){
			whol2 = whol2 << 1;
			e2--;
		}
		e = e1;
	}
	else{
		en = e1;
		e = e1;
	}
	int m = 0;
	int frac = 0;
	if(!!(val1 & (1<<8))==1 && !!(val2 & (1<<8))==1){//-x + -y = -1 * (x + y)
		m = whol1 + whol2;
		frac = (((1 << 5) - 1) & (m >> 0)); //SINGED BIT COMPLEMENT FOR NEGATIVE VALUES DON'T GIVE THE PROPER FRAC
		n = 1; //WHICH IS WHY WE ADJUST THE ORDER OF ADDITION BASED ON THE VALUES SIGNED BITS TO GET THE PROPER FRAC
	}
	else if(!!(val1 & (1<<8))==1){//-x + y = y - x
		m = whol2 - whol1;
		if(m<0){
			m*=-1;//FOR A VALUE<0, MULTPLY -1 TO GET THE RIGHT FRAC FIELD, BC WE DON'T WANT THE SIGNED BIT COMPLEMENT AKA -1 HAVING ALL 32 BITS FILLED WITH 1's
			frac = (((1 << 5) - 1) & (m >> 0));
			n = 1;
		}
		else if(m>=0){
			frac = (((1 << 5) - 1) & (m >> 0));
		}
	}
	else if(!!(val2 & (1<<8))==1){//x + -y = x - y
		m = whol1 - whol2;
		if(m<0){
			m*=-1;
			frac = (((1 << 5) - 1) & (m >> 0));
			n = 1;
		}
		else if(m>=0){
			frac = (((1 << 5) - 1) & (m >> 0));
		}
	}
	else{
		m = whol1 + whol2;
		frac = (((1 << 5) - 1) & (m >> 0));
	}
	m = m >> 5;
	e+=5;
	en+=5;
	if(abs(m)>1){ //SHIFT TO GET IN PROPER MANTISSA RANGE
		while(abs(m)!=1){
			int c = !!(m & (1<<0));
			m = m >> 1;
			frac = frac >> 1;
			if(c==1){
				frac = (frac | (1 << 4));
			}
			else if(c==0){
				frac = (frac & (~(1 << 4)));
			}
			e++;
			en++;
		}
	}
	else if(abs(m)<1){ //SHIFT TO GET IN PROPER MANTISSA RANGE
		while(abs(m)!=1){
			int l = !!(frac & (1<<4));
			m = m << 1;
			frac = frac << 1;
			if(l==1){
				m = (m | (1 << 0));
			}
			else{
				m = (m & (~(1 << 0)));
			}
			frac = (((1 << 5) - 1) & (frac >> 0));
			e--;
			en--;
		}
	}
	int ex = en + BIAS;
	int ans = 0;
	if(ex<=0){//DENORMALIZED
		while(en!=EDEN){ //UNTIL OUR BIG E IS -2, SHIFT
			z = !!(m & (1<<0));
		  	m = m >> 1;
		  	frac = frac >> 1;
		  	if(z==1){
				frac = (frac | (1 << 4));
		  	}
		  	else if(z==0){
				frac = (frac & (~(1 << 4)));
		  	}
		  	frac = (((1 << 5) - 1) & (frac >> 0));
		  	e++;
		  	en++;
	    	}
		ex = 0;
		ans = ex | frac;
		if(n==1){
			ans = ans | 256;
		}
		return ans;
	}
	else if(ex>=7){//INF
		ex = 7;
		ans = ex << 5;
		if(n==1){
			ans = ex | 256;
		}
		return ans;
	}
	else{//NORMALIZED
		ex = ex << 5;
		ans = ex | frac;
		if(n==1){
			ans = ans | 256;
		}
		return ans;
	}
	return -1;
}

// subKIFP - Subtracts two KIFP Values together using the Addition Techniques from Class.
// - To get credit, you must work with S, M, and E components.
// - You are allowed to shift/adjust M and E as needed.
// Return the resulting KIFP Value on Success or -1 on Failure.
kifp_t subKifp(kifp_t val1, kifp_t val2) {
	int fin = specSub(val1, val2);//HELPER FUNCTION FOR SUBTRACTION SPECIAL CASES
	int infin = 0x0E0;
	int notn = 0x0E1;
	if(fin==1){
		return notn;
	}
	else if(fin==2){
		return infin;
	}
	else if(fin==3){
		infin = infin | 256;
		return infin;
	}
	else if(fin==4){
		infin = 0;
		return infin;
	}
	else if(fin==5){
		infin = 256;
		return infin;
	}
	else if(fin==6){
		if(!!(val2 & (1 << 8)==0)){
			val2 = (val2 | (1 << 8));
		}
		else if(!!(val2 & (1 << 8)==1)){
			val2 = (val2 & (~(1 << 8)));
		}
		return val2;
	}
	else if(fin==7){
		if(!!(val1 & (1 << 8)==0)){
			val1 = (val1 | (1 << 8));
		}
		else if(!!(val1 & (1 << 8)==1)){
			val1 = (val1 & (~(1 << 8)));
		}
		return val1;
	}
	int e1 = 0;
        int e2 = 0;
        int whol1 = 0;
        int whol2 = 0;
        int ex1 = (((1 << 3) - 1) & (val1 >> 5));//EXP FIELDS OF VALUES
        int ex2 = (((1 << 3) - 1) & (val2 >> 5));
        int n = 0;
        int e = 0;
        int z = 0;
        if(ex1==0){//DENORMALIZED
                whol1 = 0;
                e1 = EDEN;
        }
        else{
                whol1 = 1;
                e1 = ex1 - BIAS;
        }
        if(ex2==0){//DENOARMLIZED
                whol2 = 0;
                e2 = EDEN;
        }
        else{
                whol2 = 1;
                e2 = ex2 - BIAS;
        }
        int en = 0;
        int frac1 = (((1 << 5) - 1) & (val1 >> 0));//FRAC FIELDS OF VALUES
        int frac2 = (((1 << 5) - 1) & (val2 >> 0));
        whol1 = whol1 << 5;//SHIFT TO ADD VALUES AS WHOLES, FRAC FIELD IS 5 BITS, SO WE SHIFT LEFT 5 BITS TO MAKE SPACE FOR FRAC
        e1-=5;
        whol1 = whol1 | frac1;
        whol2 = whol2 << 5;
        e2-=5;
        whol2 = whol2 | frac2;
        e = e1;
	if(e1>e2){ //E'S NEED TO BE THE SAME FOR ADDING AND SUBTRACTING
                en = e2;
                while(e1!=e2){
                        whol1 = whol1 << 1;
                        e1--;
                }
                e = e2;
        }
        else if(e2>e1){
                en = e1;
                while(e2!=e1){
                        whol2 = whol2 << 1;
                        e2--;
                }
                e = e1;
        }
        else{
                en = e1;
                e = e1;
        }
        int m = 0;
        int frac = 0;
	if(!!(val1 & (1<<8))==1 && !!(val2 & (1<<8))==1){ //-x - -y = -x + y = y + -x = y - x
                m = whol2 - whol1;
		if(m<0){
                        m*=-1;
                        frac = (((1 << 5) - 1) & (m >> 0));
                        n = 1;
                }
                else if(m>=0){
                        frac = (((1 << 5) - 1) & (m >> 0));
                }
        }
        else if(!!(val1 & (1<<8))==1){ //-x - y = -1 * (x + y)
                m = whol1 + whol2;
		frac = ((((1 << 5) - 1) & (m >> 0)));
		n = 1;
        }
        else if(!!(val2 & (1<<8))==1){ //x - -y = x + y
		m = whol1 + whol2;
                frac = ((((1 << 5) - 1) & (m >> 0)));
        }
        else{
                m = whol1 - whol2; //x - y
                if(m<0){
                        m*=-1;
                        frac = (((1 << 5) - 1) & (m >> 0));
                        n = 1;
                }
                else if(m>=0){
                        frac = (((1 << 5) - 1) & (m >> 0));
                }
        }
        m = m >> 5;
        e+=5;
        en+=5;
	if(abs(m)>1){ //SHIFT TO GET IN PROPER MANTISSA RANGE
                while(abs(m)!=1){
                        int c = !!(m & (1<<0));
                        m = m >> 1;
                        frac = frac >> 1;
                        if(c==1){
                                frac = (frac | (1 << 4));
                        }
                        else if(c==0){
                                frac = (frac & (~(1 << 4)));
                        }
                        e++;
                        en++;
                }
        }
        else if(abs(m)<1){
                while(abs(m)!=1){
                        int l = !!(frac & (1<<4));
                        m = m << 1;
                        frac = frac << 1;
                        if(l==1){
                                m = (m | (1 << 0));
                        }
                        else{
                                m = (m & (~(1 << 0)));
                        }
                        frac = (((1 << 5) - 1) & (frac >> 0));
                        e--;
                        en--;
                }
        }
        int ex = en + BIAS;
        int ans = 0;
	if(ex<=0){//DENORMALIZED
                while(en!=EDEN){
                        z = !!(m & (1<<0));
                        m = m >> 1;
                        frac = frac >> 1;
                        if(z==1){
                                frac = (frac | (1 << 4));
                        }
                        else if(z==0){
                                frac = (frac & (~(1 << 4)));
                        }
                        frac = (((1 << 5) - 1) & (frac >> 0));
                        e++;
                        en++;
                }
                ex = 0;
                ans = ex | frac;
                if(n==1){
                        ans = ans | 256;
                }
                return ans;
        }
        else if(ex>=7){//INF
                ex = 7;
                ans = ex << 5;
                if(n==1){
                        ans = ex | 256;
                }
                return ans;
        }
        else{//NORMALIZED
                ex = ex << 5;
                ans = ex | frac;
                if(n==1){
                        ans = ans | 256;
                }
                return ans;
        }
        return -1;
}

// negateKIFP - Negates a KIFP Value.
// Return the resulting KIFP Value on Success or -1 on Failure.
kifp_t negateKifp(kifp_t value) {
	int b = (value & (1 << 8)) >> 8; //GETS THE CURRENT SIGNED BIT OF OUR VALUE AND CHANGES IT
	if(b==0){
		value = (value | (1 << 8));
		return value;
	}
	else if(b==1){
		value = (value & (~(1 << 8)));
		return value;
	}
	return -1;
}
