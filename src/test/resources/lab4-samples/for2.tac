main:
	BeginFunc 44 ;
	_tmp0 = 10 ;
	i = _tmp0 ;
_L0:
	_tmp1 = 0 ;
	_tmp2 = _tmp1 < i ;
	IfZ _tmp2 Goto _L1 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp3 = 2 ;
	_tmp4 = i - _tmp3 ;
	i = _tmp4 ;
	Goto _L0 ;
_L1:
	_tmp5 = "done\n" ;
	PushParam _tmp5 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
