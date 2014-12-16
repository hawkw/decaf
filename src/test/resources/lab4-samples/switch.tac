main:
	BeginFunc 40 ;
	_tmp0 = 3 ;
	num = _tmp0 ;
	_tmp1 = 1 ;
	_tmp2 = num == _tmp1 ;
	IfZ _tmp2 Goto _L0 ;
	Goto _L2 ;
_L0:
	_tmp3 = 2 ;
	_tmp4 = num == _tmp3 ;
	IfZ _tmp4 Goto _L1 ;
	Goto _L3 ;
_L1:
	Goto _L4 ;
_L2:
	_tmp5 = 1 ;
	i = _tmp5 ;
_L3:
	_tmp6 = 10 ;
	i = _tmp6 ;
	Goto _L5 ;
_L4:
	_tmp7 = "hello" ;
	PushParam _tmp7 ;
	LCall _PrintString ;
	PopParams 4 ;
_L5:
	EndFunc ;
