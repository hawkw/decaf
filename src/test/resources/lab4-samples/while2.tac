main:
	BeginFunc 12 ;
_L0:
	_tmp0 = 1 ;
	IfZ _tmp0 Goto _L1 ;
	_tmp1 = 1 ;
	PushParam _tmp1 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L1 ;
	_tmp2 = 2 ;
	PushParam _tmp2 ;
	LCall _PrintInt ;
	PopParams 4 ;
	Goto _L0 ;
_L1:
	EndFunc ;
