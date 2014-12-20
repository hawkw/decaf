main:
	BeginFunc 20 ;
	_tmp0 = 1 ;
	IfZ _tmp0 Goto _L0 ;
	_tmp1 = 0 ;
	IfZ _tmp1 Goto _L2 ;
	_tmp2 = 1 ;
	PushParam _tmp2 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L3 ;
_L2:
	_tmp3 = 2 ;
	PushParam _tmp3 ;
	LCall _PrintInt ;
	PopParams 4 ;
_L3:
	Goto _L1 ;
_L0:
	_tmp4 = 3 ;
	PushParam _tmp4 ;
	LCall _PrintInt ;
	PopParams 4 ;
_L1:
	EndFunc ;
