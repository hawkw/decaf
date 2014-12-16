____f:
	BeginFunc 20 ;
	_tmp0 = 5 ;
	_tmp1 = 10 ;
	_tmp2 = _tmp0 * _tmp1 ;
	_tmp3 = 4 ;
	_tmp4 = _tmp2 + _tmp3 ;
	Return _tmp4 ;
	EndFunc ;
____g:
	BeginFunc 16 ;
	_tmp5 = 5 ;
	_tmp6 = a == _tmp5 ;
	IfZ _tmp6 Goto _L0 ;
	_tmp7 = "hello" ;
	Return _tmp7 ;
	Goto _L1 ;
_L0:
	_tmp8 = "world" ;
	Return _tmp8 ;
_L1:
	EndFunc ;
main:
	BeginFunc 36 ;
	_tmp9 = LCall ____f ;
	PushParam _tmp9 ;
	LCall _PrintInt ;
	PopParams 4 ;
	_tmp10 = 4 ;
	_tmp11 = 5 ;
	_tmp12 = _tmp10 * _tmp11 ;
	_tmp13 = 2 ;
	_tmp14 = _tmp12 / _tmp13 ;
	PushParam _tmp14 ;
	_tmp15 = LCall ____g ;
	PopParams 4 ;
	s = _tmp15 ;
	PushParam s ;
	LCall _PrintString ;
	PopParams 4 ;
	EndFunc ;
