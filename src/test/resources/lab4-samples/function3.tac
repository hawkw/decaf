____f:
	BeginFunc 8 ;
	_tmp0 = 1 ;
	PushParam _tmp0 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Return  ;
	_tmp1 = 2 ;
	PushParam _tmp1 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
____g:
	BeginFunc 16 ;
_L0:
	_tmp2 = 1 ;
	IfZ _tmp2 Goto _L1 ;
	_tmp3 = 3 ;
	PushParam _tmp3 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp4 = 5 ;
	Return _tmp4 ;
	_tmp5 = 4 ;
	PushParam _tmp5 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L0 ;
_L1:
	EndFunc ;
main:
	BeginFunc 8 ;
	LCall ____f ;
	_tmp6 = LCall ____g ;
	Return  ;
	_tmp7 = 5 ;
	PushParam _tmp7 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
