main:
	BeginFunc 60 ;
	_tmp0 = 3 ;
	i = _tmp0 ;
	_tmp1 = 0 ;
	i = _tmp1 ;
_L0:
	_tmp2 = 10 ;
	_tmp3 = i < _tmp2 ;
	IfZ _tmp3 Goto _L1 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp4 = 1 ;
	_tmp5 = i + _tmp4 ;
	i = _tmp5 ;
	_tmp6 = 2 ;
	_tmp7 = i * _tmp6 ;
	i = _tmp7 ;
	Goto _L0 ;
_L1:
	_tmp8 = "done\n" ;
	PushParam _tmp8 ;
	LCall _PrintString ;
	PopParams 4 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
