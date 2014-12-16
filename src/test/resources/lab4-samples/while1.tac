main:
	BeginFunc 36 ;
	_tmp0 = 0 ;
	i = _tmp0 ;
_L0:
	_tmp1 = 5 ;
	_tmp2 = i < _tmp1 ;
	IfZ _tmp2 Goto _L1 ;
	PushParam i ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp3 = 1 ;
	_tmp4 = i + _tmp3 ;
	i = _tmp4 ;
	Goto _L0 ;
_L1:
	EndFunc ;
