main:
	BeginFunc 28 ;
_L0:
	_tmp0 = 1 ;
	IfZ _tmp0 Goto _L1 ;
_L2:
	_tmp1 = 1 ;
	IfZ _tmp1 Goto _L3 ;
	_tmp2 = 1 ;
	PushParam _tmp2 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L3 ;
	_tmp3 = 2 ;
	PushParam _tmp3 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Return  ;
	Goto _L2 ;
_L3:
	_tmp4 = 3 ;
	PushParam _tmp4 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L1 ;
	_tmp5 = 4 ;
	PushParam _tmp5 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Return  ;
	Goto _L0 ;
_L1:
	_tmp6 = 5 ;
	PushParam _tmp6 ;
	LCall _PrintInt ;
	PopParams 4 ;
	EndFunc ;
