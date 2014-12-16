main:
	BeginFunc 12 ;
	_tmp0 = 1 ;
	IfZ _tmp0 Goto _L0 ;
	_tmp1 = 1 ;
	IfZ _tmp1 Goto _L2 ;
	_tmp2 = 1 ;
	PushParam _tmp2 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L3 ;
_L2:
_L3:
	Goto _L1 ;
_L0:
_L1:
	EndFunc ;
