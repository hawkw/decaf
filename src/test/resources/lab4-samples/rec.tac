____Recur:
	BeginFunc 44 ;
	_tmp0 = 0 ;
	_tmp1 = depth < _tmp0 ;
	IfZ _tmp1 Goto _L0 ;
	Return  ;
	Goto _L1 ;
_L0:
_L1:
	_tmp2 = 1000 ;
	_tmp3 = depth % _tmp2 ;
	_tmp4 = 0 ;
	_tmp5 = _tmp3 == _tmp4 ;
	IfZ _tmp5 Goto _L2 ;
	PushParam depth ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp6 = "\n" ;
	PushParam _tmp6 ;
	LCall _PrintString ;
	PopParams 4 ;
	Goto _L3 ;
_L2:
_L3:
	_tmp7 = 1 ;
	_tmp8 = depth - _tmp7 ;
	PushParam _tmp8 ;
	LCall ____Recur ;
	PopParams 4 ;
	EndFunc ;
main:
	BeginFunc 4 ;
	_tmp9 = 20000 ;
	PushParam _tmp9 ;
	LCall ____Recur ;
	PopParams 4 ;
	EndFunc ;
