____sum:
	BeginFunc 24 ;
	_tmp0 = 0 ;
	_tmp1 = s == _tmp0 ;
	IfZ _tmp1 Goto _L0 ;
	Return s ;
	Goto _L1 ;
_L0:
_L1:
	_tmp2 = 1 ;
	_tmp3 = s - _tmp2 ;
	PushParam _tmp3 ;
	_tmp4 = LCall ____sum ;
	PopParams 4 ;
	_tmp5 = s + _tmp4 ;
	Return _tmp5 ;
	EndFunc ;
main:
	BeginFunc 8 ;
	_tmp6 = 234 ;
	PushParam _tmp6 ;
	_tmp7 = LCall ____sum ;
	PopParams 4 ;
	PushParam _tmp7 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
