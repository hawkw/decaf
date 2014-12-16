main:
	BeginFunc 56 ;
	_tmp0 = 0 ;
	i = _tmp0 ;
_L0:
	_tmp1 = 5 ;
	_tmp2 = i < _tmp1 ;
	IfZ _tmp2 Goto _L1 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp3 = 3 ;
	_tmp4 = i == _tmp3 ;
	IfZ _tmp4 Goto _L2 ;
	Goto _L1 ;
	Goto _L3 ;
_L2:
_L3:
	_tmp5 = " ok\n" ;
	PushParam _tmp5 ;
	LCall _PrintString ;
	PopParams 4 ;
	_tmp6 = 1 ;
	_tmp7 = i + _tmp6 ;
	i = _tmp7 ;
	Goto _L0 ;
_L1:
	_tmp8 = "done" ;
	PushParam _tmp8 ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
